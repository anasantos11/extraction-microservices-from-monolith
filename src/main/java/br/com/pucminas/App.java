package br.com.pucminas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import br.com.pucminas.businesslogics.ServiceBL;
import br.com.pucminas.domain.Method;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

public class App {
    private static final String JAR_FILE_PATH = "src/test/resources/kanleitos-0.0.1-SNAPSHOT.jar";
    private static final String INCLUDED_PACKAGE = "br.com.kanleitos";
    private static final String REPOSITORY_PATH_NAME = "repos/kanleitos-API";
    private static final String REPOSITORY_URI = "https://github.com/anasantos11/kanleitos-API.git";

    public static void main(String... args) throws GitAPIException, IOException {
        File repositoryFolder = new File(REPOSITORY_PATH_NAME);
        Git git = null;
        try {
            git = Git.open(repositoryFolder);
        } catch (IOException ioException) {
            git = Git.cloneRepository().setURI(REPOSITORY_URI).setDirectory(repositoryFolder).call();
        }

        GitRepository gitRepository = new GitRepository(git);
        CallGraphRepository callGraphRepository = new CallGraphRepository(new File(JAR_FILE_PATH),
                Arrays.asList(INCLUDED_PACKAGE));

        try (FileWriter fileWriterAllItems = new FileWriter("microservices.log")) {
            try (PrintWriter printWriterAllItems = new PrintWriter(fileWriterAllItems, true)) {
                Map<String, Set<Method>> microservices = new ServiceBL(gitRepository, callGraphRepository, (byte) 1,
                        (byte) 1, (byte) 1, (byte) 50).groupServicesInMicroservices();

                for (Map.Entry<String, Set<Method>> entry : microservices.entrySet()) {
                    entry.getValue().forEach(service -> printWriterAllItems.println(entry.getKey() + "-"
                            + service.getClassName().getClassName() + ":" + service.getMethodName()));
                }
            }
        }
    }
}
