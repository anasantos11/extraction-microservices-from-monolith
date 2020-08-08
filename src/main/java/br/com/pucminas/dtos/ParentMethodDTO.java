package br.com.pucminas.dtos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParentMethodDTO extends BaseMethodDTO {
	private List<BaseMethodDTO> methodsCalled;

	public ParentMethodDTO(String className, String methodName) {
		super(className, methodName);
		this.methodsCalled = new ArrayList<BaseMethodDTO>();
	}

	public List<BaseMethodDTO> getMethodsCalled() {
		return this.methodsCalled;
	}

	@Override
	public String toString() {
		return super.toString() + "; Methods Called = " + Arrays.toString(methodsCalled.toArray());
	}
}
