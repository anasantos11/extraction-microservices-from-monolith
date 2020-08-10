package br.com.pucminas.dtos;

import java.util.Arrays;
import java.util.List;

public class CommitDTO {
	private String commitId;
	private String authorEmail;
	private List<String> changedFiles;

	public CommitDTO(String commitId, String authorEmail, List<String> files) {
		this.commitId = commitId;
		this.authorEmail = authorEmail;
		this.changedFiles = files;
	}

	public String getCommitId() {
		return this.commitId;
	}

	public String getAuthorEmail() {
		return this.authorEmail;
	}

	public List<String> getChangedFiles() {
		return this.changedFiles;
	}

	public String ToString() {
		return "Commit Id = " + this.commitId + "; Author's E-mail = " + this.authorEmail + "; Changed Files = "
				+ Arrays.toString(changedFiles.toArray());
	}
}
