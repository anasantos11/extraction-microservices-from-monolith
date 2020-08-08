package br.com.pucminas.repositories;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.pucminas.dtos.MethodCalledDTO;
import br.com.pucminas.dtos.ParentMethodDTO;
import gr.gousiosg.javacg.stat.JCallGraph;

public class CallGraphRepository {

	private File jarFile;
	private Map<String, ParentMethodDTO> methods;

	public CallGraphRepository(File jarFile) {
		this.jarFile = jarFile;
		this.methods = new HashMap<>();
	}

	public List<ParentMethodDTO> getCallGraph() throws IOException {
		PrintStream myStream = System.out;
		ByteArrayOutputStream resultBuffer = new ByteArrayOutputStream();
		System.setOut(new PrintStream(resultBuffer));
		JCallGraph.main(new String[] { jarFile.getPath() });
		System.setOut(myStream);

		String line;

		BufferedReader bufferedReader = new BufferedReader(new StringReader(new String(resultBuffer.toByteArray())));
		while ((line = bufferedReader.readLine()) != null) {
			fillMethod(line);
		}

		return this.methods.values().stream().collect(Collectors.toList());
	}

	private void fillMethod(String line) {
		String dataType = line.substring(0, 1);
		String[] data = line.substring(2).split(" ");

		if (dataType.equals("M")) {
			String[] parentMethodData = data[0].split(":");
			String parentClassName = parentMethodData[0];
			String parenteMethodName = parentMethodData[1];

			String[] calledMethodData = data[1].split(":");
			String classNameMethodCalled = calledMethodData[0].substring(3);
			String typeOfCall = calledMethodData[0].substring(1, 2);
			String methodNameCalled = calledMethodData[1];

			if (!this.methods.containsKey(parenteMethodName)) {
				this.methods.put(parenteMethodName, new ParentMethodDTO(parentClassName, parenteMethodName));
			}

			ParentMethodDTO method = this.methods.get(parenteMethodName);
			method.getMethodsCalled().add(new MethodCalledDTO(classNameMethodCalled, methodNameCalled, typeOfCall));
		}
	}
}
