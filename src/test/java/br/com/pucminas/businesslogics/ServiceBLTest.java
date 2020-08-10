package br.com.pucminas.businesslogics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import br.com.pucminas.domain.Method;
import br.com.pucminas.dtos.CommitDTO;
import br.com.pucminas.dtos.ParentMethodDTO;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

public class ServiceBLTest {
	@Mock
	private GitRepository mockGitRepository;

	@Mock
	private CallGraphRepository mockCallGraphRepository;

	@Before
	public void setup() {
		this.mockGitRepository = mock(GitRepository.class);
		this.mockCallGraphRepository = mock(CallGraphRepository.class);
	}

	@Test
	public void getCandidateServices() throws IOException, GitAPIException {
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

		ServiceBL service = new ServiceBL(mockGitRepository, mockCallGraphRepository);
		List<Method> results = service.getCandidateServices();

		assertNotNull(results);
		assertEquals(4, results.size());
		assertTrue(results.stream().anyMatch(
				methodResult -> methodResult.getClassName().getClassName().equals("br.com.pucminas.ClassNameA")
						&& methodResult.getMethodName().equals("MethodNameA")
						&& methodResult.getMethodsThatReferenceIt().size() == 0
						&& methodResult.getClassName().getCommitIds().size() == 1
						&& methodResult.getClassName().getCommitIds().contains("C1")
						&& methodResult.isCandidateService()));

		assertTrue(results.stream().anyMatch(
				methodResult -> methodResult.getClassName().getClassName().equals("br.com.pucminas.ClassNameB")
						&& methodResult.getMethodName().equals("MethodNameB")
						&& methodResult.getMethodsThatReferenceIt().size() == 0
						&& methodResult.getClassName().getCommitIds().size() == 1
						&& methodResult.getClassName().getCommitIds().contains("C1")
						&& methodResult.isCandidateService()));
		assertTrue(results.stream()
				.anyMatch(methodResult -> methodResult.getClassName().getClassName()
						.equals("br.com.pucminas.ClassNameMethodCalledA")
						&& methodResult.getMethodName().equals("MethodNameCalledA")
						&& methodResult.getMethodsThatReferenceIt().size() == 1
						&& methodResult.getMethodsThatReferenceIt().get(0).getClassName().getClassName()
								.equals("br.com.pucminas.ClassNameA")
						&& methodResult.getMethodsThatReferenceIt().get(0).getMethodName().equals("MethodNameA")
						&& methodResult.getClassName().getCommitIds().size() == 0
						&& !methodResult.isCandidateService()));
		assertTrue(results.stream()
				.anyMatch(methodResult -> methodResult.getClassName().getClassName()
						.equals("br.com.pucminas.ClassNameMethodCalledB")
						&& methodResult.getMethodName().equals("MethodNameCalledB")
						&& methodResult.getMethodsThatReferenceIt().size() == 1
						&& methodResult.getMethodsThatReferenceIt().get(0).getClassName().getClassName()
								.equals("br.com.pucminas.ClassNameA")
						&& methodResult.getMethodsThatReferenceIt().get(0).getMethodName().equals("MethodNameA")
						&& methodResult.getClassName().getCommitIds().size() == 0
						&& !methodResult.isCandidateService()));
	}
}
