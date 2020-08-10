package br.com.pucminas.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.com.pucminas.dtos.ParentMethodDTO;
import gr.gousiosg.javacg.stat.JCallGraph;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JCallGraph.class, CallGraphRepository.class })
public class CallGraphRepositoryTest {

	@Mock
	File mockJarFile;

	@Spy
	private CallGraphRepository callGraphRepository = new CallGraphRepository(mockJarFile, Arrays.asList("br.com.pucminas"));

	@Test
	public void getCallGraph() throws Exception {
		var mockResult = new StringBuilder();
		mockResult.append("C:br.com.pucminas.controllers.TestController br.com.pucminas.models.Test\r\n");
		mockResult.append("M:br.com.test.controllers.TestController:getTests(boolean) (I)br.com.test.repository.TestRepository:getAllById(int)\r\n");
		mockResult.append(
				"M:br.com.pucminas.controllers.TestController:listarTests(boolean) (I)br.com.pucminas.repository.TestRepository:findAllById(int)\r\n");
		mockResult.append(
				"M:br.com.pucminas.controllers.TestController:listarTests(boolean) (I)br.com.pucminas.repository.TestRepository:findAll()");

		PowerMockito.mockStatic(JCallGraph.class);
		PowerMockito.doNothing().when(JCallGraph.class, "main", (Object) new String[] {});
		PowerMockito.doReturn(mockResult.toString()).when(this.callGraphRepository, "getMethods");

		List<ParentMethodDTO> result = callGraphRepository.getCallGraph();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("br.com.pucminas.controllers.TestController", result.get(0).getClassName());
		assertEquals("listarTests(boolean)", result.get(0).getMethodName());
		assertEquals("br.com.pucminas.controllers.TestController:listarTests(boolean)", result.get(0).getFullName());
		assertEquals(2, result.get(0).getMethodsCalled().size());
		assertTrue(result.get(0).getMethodsCalled().stream().anyMatch(
				methodCalled -> methodCalled.getClassName().equals("br.com.pucminas.repository.TestRepository")
						&& methodCalled.getMethodName().equals("findAllById(int)")
						&& methodCalled.getSourceFileName().equals("br/com/pucminas/repository/TestRepository.java")
						&& methodCalled.getFullName()
								.equals("br.com.pucminas.repository.TestRepository:findAllById(int)")));
		assertTrue(result.get(0).getMethodsCalled().stream()
				.anyMatch(methodCalled -> methodCalled.getClassName().equals(
						"br.com.pucminas.repository.TestRepository") && methodCalled.getMethodName().equals("findAll()")
						&& methodCalled.getSourceFileName().equals("br/com/pucminas/repository/TestRepository.java")
						&& methodCalled.getFullName().equals("br.com.pucminas.repository.TestRepository:findAll()")));

	}
}
