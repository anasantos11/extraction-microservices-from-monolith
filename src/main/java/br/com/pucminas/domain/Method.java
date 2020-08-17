package br.com.pucminas.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Method {
	private String methodName;
	private ClassName className;
	private List<Method> methodsThatReferenceIt;
	private List<Method> methodsCalled;

	public Method(String methodName, ClassName className, List<Method> methodsThatReferenceIt,
			List<Method> methodsCalled) {
		this.methodName = methodName;
		this.className = className;
		this.methodsThatReferenceIt = methodsThatReferenceIt;
		this.methodsCalled = methodsCalled;
	}

	public Method(String methodName, ClassName className) {
		this.methodName = methodName;
		this.className = className;
		this.methodsThatReferenceIt = new ArrayList<Method>();
		this.methodsCalled = new ArrayList<Method>();
	}

	public String getMethodName() {
		return methodName;
	}

	public ClassName getClassName() {
		return className;
	}

	public List<Method> getMethodsThatReferenceIt() {
		return methodsThatReferenceIt;
	}

	public List<Method> getMethodsCalled() {
		return methodsCalled;
	}

	public boolean isCandidateService() {
		return this.methodsThatReferenceIt.size() == 0;
	}

	public List<ClassName> getClasses() {
		List<ClassName> classes = this.methodsCalled.stream().map(method -> method.getClassName())
				.collect(Collectors.toList());
		return classes;
	}

	public List<Method> getMethods() {
		List<Method> methods = this.methodsCalled;

		return methods;
	}

	public List<String> getCommitIds() {
		List<String> commits = this.methodsCalled.stream().map(method -> method.getClassName().getCommitIds())
				.flatMap(List::stream).collect(Collectors.toList());
		commits.addAll(this.className.getCommitIds());

		return commits;
	}

	@Override
	public String toString() {
		return this.className.toString() + "; Method Name = " + this.methodName
				+ (this.methodsThatReferenceIt.size() > 0
						? "; Methods That Reference It = " + Arrays.toString(methodsThatReferenceIt.toArray())
						: "")
				+ Arrays.toString(this.className.getCommitIds().toArray());
	}
}
