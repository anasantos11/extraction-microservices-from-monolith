package br.com.pucminas.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Method {
	private String methodName;
	private ClassName className;
	private List<Method> methodsThatReferenceIt;

	public Method(String methodName, ClassName className, List<Method> methodsThatReferenceIt) {
		this.methodName = methodName;
		this.className = className;
		this.methodsThatReferenceIt = methodsThatReferenceIt;
	}

	public Method(String methodName, ClassName className) {
		this.methodName = methodName;
		this.className = className;
		this.methodsThatReferenceIt = new ArrayList<Method>();
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

	public boolean isCandidateService() {
		return this.methodsThatReferenceIt.size() == 0;
	}

	@Override
	public String toString() {
		return this.className.toString() + "; Method Name = " + this.methodName
				+ (this.methodsThatReferenceIt.size() > 0
						? "; Methods That Reference It = " + Arrays.toString(methodsThatReferenceIt.toArray())
						: "");
	}
}
