package br.com.pucminas.businesslogics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import br.com.pucminas.domain.ClassName;
import br.com.pucminas.domain.Method;
import br.com.pucminas.domain.SimilarityMatrixCell;
import br.com.pucminas.dtos.CommitDTO;
import br.com.pucminas.dtos.ParentMethodDTO;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ServiceBL.class })
public class ServiceBLTest {
	@Mock
	private GitRepository mockGitRepository;

	@Mock
	private CallGraphRepository mockCallGraphRepository;

	@Spy
	private ServiceBL serviceBL = new ServiceBL(mockGitRepository, mockCallGraphRepository, (byte) 1, (byte) 1,
			(byte) 1, (byte) 80);

	@Before
	public void setup() {
		this.mockGitRepository = mock(GitRepository.class);
		this.mockCallGraphRepository = mock(CallGraphRepository.class);
	}

	@Test
	public void getCandidateServices() throws Exception {
		ParentMethodDTO parentMethodA = new ParentMethodDTO("br.com.pucminas.ClassNameA", "MethodNameA");
		ParentMethodDTO parentMethodB = new ParentMethodDTO("br.com.pucminas.ClassNameB", "MethodNameB");
		ParentMethodDTO methodCalledA = new ParentMethodDTO("br.com.pucminas.ClassNameMethodCalledA",
				"MethodNameCalledA");
		ParentMethodDTO methodCalledB = new ParentMethodDTO("br.com.pucminas.ClassNameMethodCalledB",
				"MethodNameCalledB");
		parentMethodA.getMethodsCalled().add(methodCalledA);
		parentMethodA.getMethodsCalled().add(methodCalledB);

		List<ParentMethodDTO> mockMethods = new ArrayList<ParentMethodDTO>();
		mockMethods.add(parentMethodA);
		mockMethods.add(methodCalledA);
		mockMethods.add(methodCalledB);
		mockMethods.add(parentMethodB);

		List<CommitDTO> mockCommits = new ArrayList<CommitDTO>();
		mockCommits.add(new CommitDTO("C1", "teste@pucminas.com.br",
				Arrays.asList("br/com/pucminas/ClassNameA.java", "br/com/pucminas/ClassNameB.java")));

		when(mockGitRepository.getCommits()).thenReturn(mockCommits);
		when(mockCallGraphRepository.getCallGraph()).thenReturn(mockMethods);

		ServiceBL service = new ServiceBL(mockGitRepository, mockCallGraphRepository, (byte) 1, (byte) 1, (byte) 1,
				(byte) 80);
		List<Method> results = WhiteboxImpl.invokeMethod(service, "getCandidateServices");

		assertNotNull(results);
		assertEquals(4, results.size());
		assertTrue(results.stream().anyMatch(
				methodResult -> methodResult.getClassName().getName().equals("br.com.pucminas.ClassNameA")
						&& methodResult.getMethodName().equals("MethodNameA")
						&& methodResult.getMethodsThatReferenceIt().size() == 0
						&& methodResult.getMethodsCalled().size() == 2
						&& methodResult.getClassName().getCommitIds().size() == 1
						&& methodResult.getClassName().getCommitIds().contains("C1")
						&& methodResult.isCandidateService()));

		assertTrue(results.stream().anyMatch(
				methodResult -> methodResult.getClassName().getName().equals("br.com.pucminas.ClassNameB")
						&& methodResult.getMethodName().equals("MethodNameB")
						&& methodResult.getMethodsThatReferenceIt().size() == 0
						&& methodResult.getMethodsCalled().size() == 0
						&& methodResult.getClassName().getCommitIds().size() == 1
						&& methodResult.getClassName().getCommitIds().contains("C1")
						&& methodResult.isCandidateService()));
		assertTrue(results.stream()
				.anyMatch(methodResult -> methodResult.getClassName().getName()
						.equals("br.com.pucminas.ClassNameMethodCalledA")
						&& methodResult.getMethodName().equals("MethodNameCalledA")
						&& methodResult.getMethodsThatReferenceIt().size() == 1
						&& methodResult.getMethodsCalled().size() == 0
						&& methodResult.getMethodsThatReferenceIt().get(0).getClassName().getName()
								.equals("br.com.pucminas.ClassNameA")
						&& methodResult.getMethodsThatReferenceIt().get(0).getMethodName().equals("MethodNameA")
						&& methodResult.getClassName().getCommitIds().size() == 0
						&& !methodResult.isCandidateService()));
		assertTrue(results.stream()
				.anyMatch(methodResult -> methodResult.getClassName().getName()
						.equals("br.com.pucminas.ClassNameMethodCalledB")
						&& methodResult.getMethodName().equals("MethodNameCalledB")
						&& methodResult.getMethodsThatReferenceIt().size() == 1
						&& methodResult.getMethodsCalled().size() == 0
						&& methodResult.getMethodsThatReferenceIt().get(0).getClassName().getName()
								.equals("br.com.pucminas.ClassNameA")
						&& methodResult.getMethodsThatReferenceIt().get(0).getMethodName().equals("MethodNameA")
						&& methodResult.getClassName().getCommitIds().size() == 0
						&& !methodResult.isCandidateService()));
	}

	@Test
	public void groupServicesInMicroservices() throws Exception {
		SimilarityMatrixCell[][] similarityMatrix = new SimilarityMatrixCell[3][3];
		similarityMatrix[0][0] = new SimilarityMatrixCell(
				new Method("MethodA", new ClassName("br.com.pucminas.ClassA", "br/com/pucminas/ClassA")),
				new Method("MethodA", new ClassName("br.com.pucminas.ClassA", "br/com/pucminas/ClassA")), 100);
		similarityMatrix[0][1] = new SimilarityMatrixCell(
				new Method("MethodA", new ClassName("br.com.pucminas.ClassA", "br/com/pucminas/ClassA")),
				new Method("MethodB", new ClassName("br.com.pucminas.ClassB", "br/com/pucminas/ClassB")), 10);
		similarityMatrix[0][2] = new SimilarityMatrixCell(
				new Method("MethodA", new ClassName("br.com.pucminas.ClassA", "br/com/pucminas/ClassA")),
				new Method("MethodC", new ClassName("br.com.pucminas.ClassC", "br/com/pucminas/ClassC")), 20);

		similarityMatrix[1][0] = new SimilarityMatrixCell(
				new Method("MethodB", new ClassName("br.com.pucminas.ClassB", "br/com/pucminas/ClassB")),
				new Method("MethodA", new ClassName("br.com.pucminas.ClassA", "br/com/pucminas/ClassA")), 30);
		similarityMatrix[1][1] = new SimilarityMatrixCell(
				new Method("MethodB", new ClassName("br.com.pucminas.ClassB", "br/com/pucminas/ClassB")),
				new Method("MethodB", new ClassName("br.com.pucminas.ClassB", "br/com/pucminas/ClassB")), 100);
		similarityMatrix[1][2] = new SimilarityMatrixCell(
				new Method("MethodB", new ClassName("br.com.pucminas.ClassB", "br/com/pucminas/ClassB")),
				new Method("MethodC", new ClassName("br.com.pucminas.ClassC", "br/com/pucminas/ClassC")), 80);

		similarityMatrix[2][0] = new SimilarityMatrixCell(
				new Method("MethodC", new ClassName("br.com.pucminas.ClassC", "br/com/pucminas/ClassC")),
				new Method("MethodA", new ClassName("br.com.pucminas.ClassA", "br/com/pucminas/ClassA")), 40);
		similarityMatrix[2][1] = new SimilarityMatrixCell(
				new Method("MethodC", new ClassName("br.com.pucminas.ClassC", "br/com/pucminas/ClassC")),
				new Method("MethodB", new ClassName("br.com.pucminas.ClassB", "br/com/pucminas/ClassB")), 50);
		similarityMatrix[2][2] = new SimilarityMatrixCell(
				new Method("MethodC", new ClassName("br.com.pucminas.ClassC", "br/com/pucminas/ClassC")),
				new Method("MethodC", new ClassName("br.com.pucminas.ClassC", "br/com/pucminas/ClassC")), 100);

		PowerMockito.doReturn(similarityMatrix).when(this.serviceBL, "generateSimilarityMatrix");

		Map<String, Set<Method>> results = serviceBL.groupServicesInMicroservices();

		assertEquals(2, results.size());
		assertEquals(1, results.get("M0").size());
		assertEquals(2, results.get("M1").size());
		assertTrue("M0 not contain MethodA", results.get("M0")
				.contains(new Method("MethodA", new ClassName("br.com.pucminas.ClassA", "br/com/pucminas/ClassA"))));
		assertTrue("M1 not contain MethodB", results.get("M1")
				.contains(new Method("MethodB", new ClassName("br.com.pucminas.ClassB", "br/com/pucminas/ClassB"))));
		assertTrue("M1 not contain MethodC", results.get("M1")
				.contains(new Method("MethodC", new ClassName("br.com.pucminas.ClassC", "br/com/pucminas/ClassC"))));
	}

}
