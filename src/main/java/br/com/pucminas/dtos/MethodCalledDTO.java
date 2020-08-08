package br.com.pucminas.dtos;

public class MethodCalledDTO extends BaseMethodDTO {
	private String typeOfCall;

	public MethodCalledDTO(String className, String methodName, String typeOfCall) {
		super(className, methodName);
		this.typeOfCall = typeOfCall;
	}

	public String getTypeOfCall() {
		return this.typeOfCall;
	}

	@Override
	public String toString() {
		return super.toString() + "; Type of Call = " + this.typeOfCall;
	}
}
