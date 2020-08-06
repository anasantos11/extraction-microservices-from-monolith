package br.com.pucminas.dtos;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class CommitDTO {
	@Getter
	private String commitId;
	@Getter
	private String authorEmail;
	@Getter
	private List<String> changedFiles;

	public CommitDTO(String commitId, String authorEmail, List<String> files) {
		this.commitId = commitId;
		this.authorEmail = authorEmail;
		this.changedFiles = files;
	}

	public String ToString() {
		return "Commit Id = " + this.commitId + "; Author's E-mail = " + this.authorEmail + "; Changed Files = "
				+ Arrays.toString(changedFiles.toArray());
	}
}
