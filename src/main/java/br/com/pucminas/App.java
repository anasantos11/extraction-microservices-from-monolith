package br.com.pucminas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import br.com.pucminas.businesslogics.MicroserviceBL;
import br.com.pucminas.businesslogics.ServiceBL;
import br.com.pucminas.domain.Method;
import br.com.pucminas.dtos.ResultDTO;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

public class App {
    private static final String JAR_FILE_PATH = "src/test/resources/kanleitos-0.0.1-SNAPSHOT.jar";
    private static final String INCLUDED_PACKAGE = "br.com.kanleitos";
    private static final String REPOSITORY_PATH_NAME = "target/repos/kanleitos-API";
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

        List<List<String>> parameters = new ArrayList<>();
        parameters.add(Arrays.asList("1", "1", "1", "10"));
        parameters.add(Arrays.asList("1", "1", "1", "20"));
        parameters.add(Arrays.asList("1", "1", "1", "30"));
        parameters.add(Arrays.asList("1", "1", "1", "40"));
        parameters.add(Arrays.asList("1", "1", "1", "50"));
        parameters.add(Arrays.asList("1", "1", "1", "60"));
        parameters.add(Arrays.asList("1", "1", "1", "70"));
        parameters.add(Arrays.asList("1", "1", "1", "80"));
        parameters.add(Arrays.asList("1", "1", "1", "90"));

        parameters.add(Arrays.asList("1", "0", "0", "10"));
        parameters.add(Arrays.asList("1", "0", "0", "20"));
        parameters.add(Arrays.asList("1", "0", "0", "30"));
        parameters.add(Arrays.asList("1", "0", "0", "40"));
        parameters.add(Arrays.asList("1", "0", "0", "50"));
        parameters.add(Arrays.asList("1", "0", "0", "60"));
        parameters.add(Arrays.asList("1", "0", "0", "70"));
        parameters.add(Arrays.asList("1", "0", "0", "80"));
        parameters.add(Arrays.asList("1", "0", "0", "90"));

        parameters.add(Arrays.asList("0", "1", "0", "10"));
        parameters.add(Arrays.asList("0", "1", "0", "20"));
        parameters.add(Arrays.asList("0", "1", "0", "30"));
        parameters.add(Arrays.asList("0", "1", "0", "40"));
        parameters.add(Arrays.asList("0", "1", "0", "50"));
        parameters.add(Arrays.asList("0", "1", "0", "60"));
        parameters.add(Arrays.asList("0", "1", "0", "70"));
        parameters.add(Arrays.asList("0", "1", "0", "80"));
        parameters.add(Arrays.asList("0", "1", "0", "90"));

        parameters.add(Arrays.asList("0", "0", "1", "10"));
        parameters.add(Arrays.asList("0", "0", "1", "20"));
        parameters.add(Arrays.asList("0", "0", "1", "30"));
        parameters.add(Arrays.asList("0", "0", "1", "40"));
        parameters.add(Arrays.asList("0", "0", "1", "50"));
        parameters.add(Arrays.asList("0", "0", "1", "60"));
        parameters.add(Arrays.asList("0", "0", "1", "70"));
        parameters.add(Arrays.asList("0", "0", "1", "80"));
        parameters.add(Arrays.asList("0", "0", "1", "90"));

        parameters.forEach(parameter -> {
            try {
                generateSuggestions(gitRepository, callGraphRepository, Byte.parseByte(parameter.get(0)),
                        Byte.parseByte(parameter.get(1)), Byte.parseByte(parameter.get(2)),
                        Byte.parseByte(parameter.get(3)));
            } catch (IOException | GitAPIException e) {
                e.printStackTrace();
            }
        });

    }

    private static void generateSuggestions(GitRepository gitRepository, CallGraphRepository callGraphRepository,
            byte weightClassItem, byte weightMethodItem, byte weightHistoryItem, byte lowerLimitToGroup)
            throws IOException, GitAPIException {

        try (FileWriter fileWriterAllItems = new FileWriter("microservices.log", true)) {
            try (PrintWriter printWriterAllItems = new PrintWriter(fileWriterAllItems, true)) {

                Map<String, Set<Method>> microservices = new ServiceBL(gitRepository, callGraphRepository,
                        weightClassItem, weightMethodItem, weightHistoryItem, lowerLimitToGroup)
                                .groupServicesInMicroservices();
                ResultDTO result = new MicroserviceBL(microservices).generateResultsFromMicroservicesSuggestions();
                result.getMicroservices()
                        .forEach(microservice -> microservice.getServices()
                                .forEach(service -> printWriterAllItems.println(weightClassItem + "-" + weightMethodItem
                                        + "-" + weightHistoryItem + "-" + lowerLimitToGroup + "-"
                                        + result.getNumberMicroservices() + "-" + microservice.getName() + "-"
                                        + microservice.getNumberClasses() + "-" + microservice.getNumberMethods() + "-"
                                        + microservice.getNumberServices() + "-" + service.getFullMethodName())));
            }
        }
    }
}
