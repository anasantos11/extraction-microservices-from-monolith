package br.com.pucminas.domain;

import java.util.ArrayList;
import java.util.List;

public class ClassName {
	private String className;
	private String sourceFileName;
	private List<String> commitIds;

	public ClassName(String className, String sourceFileName, List<String> commitIds) {
		this.className = className;
		this.sourceFileName = sourceFileName;
		this.commitIds = commitIds;
	}

	public ClassName(String className, String sourceFileName) {
		this.className = className;
		this.sourceFileName = sourceFileName;
		this.commitIds = new ArrayList<>();
	}

	public String getClassName() {
		return this.className;
	}

	public String getSourceFileName() {
		return this.sourceFileName;
	}

	public List<String> getCommitIds() {
		return this.commitIds;
	}

	@Override
	public boolean equals(Object comparedObject) {
		if (this == comparedObject)
			return true;

		if (comparedObject == null || getClass() != comparedObject.getClass())
			return false;

		ClassName comparedClassName = (ClassName) comparedObject;
		return this.className.equals(comparedClassName.getClassName());
	}

	@Override
	public int hashCode() {
		return (31 + this.className.hashCode()) * 31 + this.sourceFileName.hashCode();
	}

	@Override
	public String toString() {
		return "Class Name = " + this.className;
	}
}
