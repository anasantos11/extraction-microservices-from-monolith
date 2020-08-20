package br.com.pucminas.businesslogics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import br.com.pucminas.domain.ClassName;
import br.com.pucminas.domain.Method;
import br.com.pucminas.domain.SimilarityMatrixCell;
import br.com.pucminas.dtos.CommitDTO;
import br.com.pucminas.dtos.ParentMethodDTO;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

public class ServiceBL {
	private GitRepository gitRepository;
	private CallGraphRepository callGraphRepository;
	private byte weightClassItem;
	private byte weightMethodItem;
	private byte weightHistoryItem;
	private byte lowerLimitToGroup;

	public ServiceBL(GitRepository gitRepository, CallGraphRepository callGraphRepository, byte weightClassItem,
			byte weightMethodItem, byte weightHistoryItem, byte lowerLimitToGroup) {
		this.gitRepository = gitRepository;
		this.callGraphRepository = callGraphRepository;
		this.weightClassItem = weightClassItem;
		this.weightMethodItem = weightMethodItem;
		this.weightHistoryItem = weightHistoryItem;
		this.lowerLimitToGroup = lowerLimitToGroup;
	}

	public Map<String, List<Method>> groupServicesInMicroservices() throws IOException, GitAPIException {
		SimilarityMatrixCell[][] similarityMatrix = generateSimilarityMatrix();
		Map<String, List<Method>> microservices = new HashMap<>();
		int numberOfMicroservices = 0;

		for (int r = 0; r < similarityMatrix.length; r++) {
			List<Method> similarServices = new ArrayList<>();
			similarServices.add(similarityMatrix[r][0].getLineMethod());

			for (int c = 0; c < similarityMatrix.length; c++) {
				SimilarityMatrixCell similarity = similarityMatrix[r][c];

				if (similarity.getSimilarity() >= lowerLimitToGroup)
					similarServices.add(similarity.getLineMethod());
			}

			if (anyMicroserviceContainsAnySimilarService(similarServices, microservices)) {
				addSimilarServicesInExistsMicroservice(similarServices, microservices);
			} else {
				microservices.put("M" + numberOfMicroservices, similarServices);
				++numberOfMicroservices;
			}
		}

		return microservices;
	}

	private boolean anyMicroserviceContainsAnySimilarService(List<Method> similarServices,
			Map<String, List<Method>> microservices) {
		return similarServices.stream().anyMatch(
				service -> microservices.values().stream().anyMatch(microservice -> microservice.contains(service)));
	}

	private void addSimilarServicesInExistsMicroservice(List<Method> similarServices,
			Map<String, List<Method>> microservices) {
		for (List<Method> services : microservices.values()) {
			if (similarServices.stream().anyMatch(service -> services.contains(service))) {
				services.addAll(similarServices);
				return;
			}
		}
	}

	public List<Method> getCandidateServices() throws IOException, GitAPIException {
		List<Method> methods = new ArrayList<Method>();
		List<ParentMethodDTO> methodsDiscovered = this.callGraphRepository.getCallGraph();
		List<CommitDTO> commitsDiscovered = this.gitRepository.getCommits();

		for (ParentMethodDTO parentMethodDTO : methodsDiscovered) {
			List<Method> methodsThatReferenceIt = methodsDiscovered.stream()
					.filter(method -> !method.getFullName().equals(parentMethodDTO.getFullName())
							&& method.getMethodsCalled().stream().anyMatch(
									methodCalled -> methodCalled.getFullName().equals(parentMethodDTO.getFullName())))
					.map(methodCalled -> new Method(methodCalled.getMethodName(),
							new ClassName(methodCalled.getClassName(), methodCalled.getSourceFileName())))
					.collect(Collectors.toList());

			List<Method> methodsCalled = parentMethodDTO.getMethodsCalled().stream()
					.map(methodCalled -> new Method(methodCalled.getMethodName(),
							new ClassName(methodCalled.getClassName(), methodCalled.getSourceFileName())))
					.collect(Collectors.toList());

			List<String> commitIds = commitsDiscovered.stream()
					.filter(commit -> commit.getChangedFiles().contains(parentMethodDTO.getSourceFileName()))
					.map(commit -> commit.getCommitId()).collect(Collectors.toList());

			var method = new Method(parentMethodDTO.getMethodName(),
					new ClassName(parentMethodDTO.getClassName(), parentMethodDTO.getSourceFileName(), commitIds),
					methodsThatReferenceIt, methodsCalled);
			methods.add(method);
		}
		return methods;
	}

	private SimilarityMatrixCell[][] generateSimilarityMatrix() throws IOException, GitAPIException {
		List<Method> candidateServices = getCandidateServices().stream()
				.filter(candidateService -> candidateService.isCandidateService()).collect(Collectors.toList());

		SimilarityMatrixCell[][] similarityMatrix = new SimilarityMatrixCell[candidateServices.size()][candidateServices
				.size()];
		int rowIndex = 0;
		int columnIndex = 0;

		for (Method lineMethod : candidateServices) {
			for (Method columnMethod : candidateServices) {
				similarityMatrix[rowIndex][columnIndex] = new SimilarityMatrixCell(lineMethod, columnMethod,
						calculateSimilarity(lineMethod, columnMethod));
			}
		}

		return similarityMatrix;
	}

	private double calculateSimilarity(Method a, Method b) {
		long numberEqualClasses = a.getClasses().stream().filter(className -> b.getClasses().contains(className))
				.count();
		long numberEqualMethods = a.getMethods().stream().filter(method -> b.getMethods().contains(method)).count();
		long numberEqualCommitIds = a.getCommitIds().stream().filter(commitId -> b.getCommitIds().contains(commitId))
				.count();
		long numberClasses = a.getClasses().size();
		long numberMethods = a.getMethods().size();
		long numberCommitIds = a.getCommitIds().size();

		long numerator = numberEqualClasses * this.weightClassItem + numberEqualMethods * this.weightMethodItem
				+ numberEqualCommitIds * this.weightHistoryItem;
		long denominator = numberClasses * this.weightClassItem + numberMethods * this.weightMethodItem
				+ numberCommitIds * this.weightHistoryItem;

		return denominator > 0 ? numerator / denominator * 100 : 0;
	}
}
