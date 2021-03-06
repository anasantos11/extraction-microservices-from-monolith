package br.com.pucminas.businesslogics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import br.com.pucminas.App;
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

	public Map<String, Set<Method>> groupServicesInMicroservices() throws IOException, GitAPIException {
		SimilarityMatrixCell[][] similarityMatrix = generateSimilarityMatrix();
		Map<String, Set<Method>> microservices = new HashMap<>();
		int numberOfMicroservices = 0;

		for (int r = 0; r < similarityMatrix.length; r++) {
			Set<Method> similarServices = new HashSet<>();

			for (int c = 0; c < similarityMatrix.length; c++) {
				SimilarityMatrixCell similarity = similarityMatrix[r][c];

				if (similarity.getSimilarity() >= lowerLimitToGroup)
					similarServices.add(similarity.getColumnMethod());
			}

			if (anyMicroserviceContainsAnySimilarService(similarServices, microservices)) {
				addSimilarServicesInExistsMicroservice(similarServices, microservices);
			} else {
				++numberOfMicroservices;
				microservices.put("M" + String.format("%02d", numberOfMicroservices), similarServices);
			}
		}

		return microservices;
	}

	private boolean anyMicroserviceContainsAnySimilarService(Set<Method> similarServices,
			Map<String, Set<Method>> microservices) {
		return similarServices.stream().anyMatch(
				service -> microservices.values().stream().anyMatch(microservice -> microservice.contains(service)));
	}

	private void addSimilarServicesInExistsMicroservice(Set<Method> similarServices,
			Map<String, Set<Method>> microservices) {
		for (Set<Method> services : microservices.values()) {
			if (similarServices.stream().anyMatch(service -> services.contains(service))) {
				services.addAll(similarServices);
				return;
			}
		}
	}

	private List<Method> getCandidateServices() throws IOException, GitAPIException {
		List<Method> methods = new ArrayList<>();
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
					.filter(commit -> commit.getChangedFiles().stream()
							.anyMatch(changedFile -> changedFile.endsWith(parentMethodDTO.getSourceFileName())))
					.map(CommitDTO::getCommitId).collect(Collectors.toList());

			var method = new Method(parentMethodDTO.getMethodName(),
					new ClassName(parentMethodDTO.getClassName(), parentMethodDTO.getSourceFileName(), commitIds),
					methodsThatReferenceIt, methodsCalled);
			methods.add(method);
		}
		try (FileWriter fileWriter = new FileWriter("methods.log", true)) {
			try (PrintWriter printWriter = new PrintWriter(fileWriter, true)) {
				methods.forEach(method -> printWriter.println(
						App.REPOSITORY_NAME + ";" + method.getFullMethodName() + ";" + method.isCandidateService()));
			}
		}
		return methods;
	}

	private SimilarityMatrixCell[][] generateSimilarityMatrix() throws IOException, GitAPIException {
		List<Method> candidateServices = getCandidateServices().stream().filter(Method::isCandidateService)
				.collect(Collectors.toList());

		SimilarityMatrixCell[][] similarityMatrix = new SimilarityMatrixCell[candidateServices.size()][candidateServices
				.size()];
		int rowIndex = 0;
		try (FileWriter fileWriter = new FileWriter("similarity-matrix.log", true)) {
			try (PrintWriter printWriter = new PrintWriter(fileWriter, true)) {

				for (Method lineMethod : candidateServices) {
					int columnIndex = 0;

					for (Method columnMethod : candidateServices) {
						double similarity = calculateSimilarity(lineMethod, columnMethod);

						printWriter.println(App.REPOSITORY_NAME + ";" + weightClassItem + ";" + weightMethodItem + ";"
								+ weightHistoryItem + ";" + lowerLimitToGroup + ";" + lineMethod.getFullMethodName()
								+ ";" + columnMethod.getFullMethodName() + ";" + similarity);

						similarityMatrix[rowIndex][columnIndex] = new SimilarityMatrixCell(lineMethod, columnMethod,
								similarity);
						++columnIndex;
					}
					++rowIndex;
				}
			}
		}

		return similarityMatrix;
	}

	private double calculateSimilarity(Method a, Method b) {
		if (a.equals(b))
			return 100;

		Set<ClassName> aDistinctClasses = a.getClasses();
		Set<Method> aDistinctMethods = a.getMethods();
		Set<String> aDistinctCommits = a.getCommitIds();

		Set<ClassName> bDistinctClasses = b.getClasses();
		Set<Method> bDistinctMethods = b.getMethods();
		Set<String> bDistinctCommits = b.getCommitIds();

		double numberEqualClasses = aDistinctClasses.stream().filter(className -> bDistinctClasses.contains(className))
				.count();
		double numberEqualMethods = aDistinctMethods.stream().filter(method -> bDistinctMethods.contains(method))
				.count();
		double numberEqualCommitIds = aDistinctCommits.stream().filter(commitId -> bDistinctCommits.contains(commitId))
				.count();

		Set<ClassName> allDistinctClasses = new HashSet<>(aDistinctClasses);
		allDistinctClasses.addAll(bDistinctClasses);

		Set<Method> allDistinctMethods = new HashSet<>(aDistinctMethods);
		allDistinctMethods.addAll(bDistinctMethods);

		Set<String> allDistinctCommits = new HashSet<>(aDistinctCommits);
		allDistinctCommits.addAll(bDistinctCommits);

		double numberTotalClasses = allDistinctClasses.size();
		double numberTotalMethods = allDistinctMethods.size();
		double numberTotalCommitIds = allDistinctCommits.size();

		double numerator = numberEqualClasses * this.weightClassItem + numberEqualMethods * this.weightMethodItem
				+ numberEqualCommitIds * this.weightHistoryItem;
		double denominator = numberTotalClasses * this.weightClassItem + numberTotalMethods * this.weightMethodItem
				+ numberTotalCommitIds * this.weightHistoryItem;

		return denominator > 0 ? (numerator / denominator) * 100 : 0;
	}
}
