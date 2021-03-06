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
	private List<String> includedPackages;

	public CallGraphRepository(File jarFile, List<String> includedPackages) {
		this.jarFile = jarFile;
		this.methods = new HashMap<>();
		this.includedPackages = includedPackages;
	}

	public List<ParentMethodDTO> getCallGraph() throws IOException {
		String line;
		try (BufferedReader bufferedReader = new BufferedReader(new StringReader(getMethods()))) {
			while ((line = bufferedReader.readLine()) != null) {
				fillMethod(line);
			}
		}

		return this.methods.values().stream().collect(Collectors.toList());
	}

	private String getMethods() {
		PrintStream standardStream = System.out;
		ByteArrayOutputStream resultBuffer = new ByteArrayOutputStream();
		System.setOut(new PrintStream(resultBuffer));
		JCallGraph.main(new String[] { jarFile.getPath() });
		System.setOut(standardStream);

		return new String(resultBuffer.toByteArray());
	}

	private void fillMethod(String line) {
		String dataType = line.substring(0, 1);
		String[] data = line.substring(2).split(" ");

		if (dataType.equals("M")) {
			String[] parentMethodData = data[0].split(":");
			String parentClassName = parentMethodData[0];
			String parenteMethodName = parentMethodData[1];

			if (includedPackages.stream().noneMatch(parentClassName::startsWith)
					|| parenteMethodName.startsWith("<init>") || parenteMethodName.startsWith("<clinit>"))
				return;

			if (parenteMethodName.equals("toString()") || parenteMethodName.equals("hashCode()")
					|| parenteMethodName.equals("equals(java.lang.Object)"))
				return;

			String[] calledMethodData = data[1].split(":");
			String classNameMethodCalled = calledMethodData[0].substring(3);
			String typeOfCall = calledMethodData[0].substring(1, 2);
			String methodNameCalled = calledMethodData[1];

			if (includedPackages.stream().noneMatch(classNameMethodCalled::startsWith)
					|| methodNameCalled.startsWith("<init>") || methodNameCalled.startsWith("<clinit>"))
				return;

			if (methodNameCalled.equals("toString()") || methodNameCalled.equals("hashCode()")
					|| methodNameCalled.equals("equals(java.lang.Object)"))
				return;

			if (!this.methods.containsKey(data[0])) {
				this.methods.put(data[0], new ParentMethodDTO(parentClassName, parenteMethodName));
			}

			ParentMethodDTO method = this.methods.get(data[0]);
			method.getMethodsCalled().add(new MethodCalledDTO(classNameMethodCalled, methodNameCalled, typeOfCall));

		}
	}
}
