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
		this.methodsThatReferenceIt = new ArrayList<>();
		this.methodsCalled = new ArrayList<>();
	}

	public String getMethodName() {
		return methodName;
	}

	public String getFullMethodName() {
		return this.className.getClassName() + ":" + this.methodName;
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
		return this.methodsThatReferenceIt.isEmpty();
	}

	public List<ClassName> getClasses() {
		return this.methodsCalled.stream().map(Method::getClassName).collect(Collectors.toList());
	}

	public List<Method> getMethods() {
		return this.methodsCalled;
	}

	public List<String> getCommitIds() {
		List<String> commits = this.methodsCalled.stream().map(method -> method.getClassName().getCommitIds())
				.flatMap(List::stream).collect(Collectors.toList());
		commits.addAll(this.className.getCommitIds());

		return commits;
	}

	@Override
	public boolean equals(Object comparedObject) {
		if (this == comparedObject)
			return true;

		if (comparedObject == null || getClass() != comparedObject.getClass())
			return false;

		Method comparedMethod = (Method) comparedObject;
		return this.className.equals(comparedMethod.getClassName())
				&& this.methodName.equals(comparedMethod.getMethodName());
	}

	@Override
	public int hashCode() {
		return (31 + this.methodName.hashCode()) * 31 + this.className.hashCode();
	}

	@Override
	public String toString() {
		return this.className.toString() + "; Method Name = " + this.methodName
				+ (!this.methodsThatReferenceIt.isEmpty()
						? "; Methods That Reference It = " + Arrays.toString(methodsThatReferenceIt.toArray())
						: "")
				+ Arrays.toString(this.className.getCommitIds().toArray());
	}
}
