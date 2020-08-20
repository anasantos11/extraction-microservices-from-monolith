package br.com.pucminas.domain;

public class SimilarityMatrixCell {
	private Method lineMethod;
	private Method columnMethod;
	private double similarity;

	public SimilarityMatrixCell(Method lineMethod, Method columnMethod, double similarity) {
		this.lineMethod = lineMethod;
		this.columnMethod = columnMethod;
		this.similarity = similarity;
	}

	public Method getLineMethod() {
		return lineMethod;
	}

	public Method getColumnMethod() {
		return columnMethod;
	}

	public double getSimilarity() {
		return similarity;
	}
}
