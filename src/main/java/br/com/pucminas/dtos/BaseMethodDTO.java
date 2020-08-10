package br.com.pucminas.dtos;

public class BaseMethodDTO {
	private String className;
	private String methodName;

	public BaseMethodDTO(String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getSourceFileName() {
		return className.replace('.', '/') + ".java";
	}

	public String getFullName() {
		return this.className + ":" + this.methodName;
	}

	@Override
	public String toString() {
		return "Class Name = " + this.className + "; Method Name = " + this.methodName;
	}
}
