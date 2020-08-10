package br.com.pucminas.repositories;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import br.com.pucminas.dtos.CommitDTO;

public class GitRepository {
	private Git git;

	public GitRepository(Git git) {
		this.git = git;
	}

	public List<CommitDTO> getCommits() throws GitAPIException, IOException {
		List<CommitDTO> commits = new ArrayList<CommitDTO>();
		DiffFormatter formatter = new DiffFormatter(new ByteArrayOutputStream());
		formatter.setRepository(this.git.getRepository());

		for (RevCommit commit : this.git.log().call()) {
			RevTree parentCommitTree = commit.getParentCount() > 0 ? commit.getParent(0).getTree() : null;
			RevTree commitTree = commit.getTree();
			List<DiffEntry> differencesBetweenTrees = formatter.scan(parentCommitTree, commitTree);
			List<String> changedFiles = differencesBetweenTrees.stream().map(diff -> diff.getNewPath())
					.collect(Collectors.toList());

			commits.add(new CommitDTO(commit.getName(), commit.getAuthorIdent().getEmailAddress(), changedFiles));
		}

		formatter.close();
		return commits;
	}
}
