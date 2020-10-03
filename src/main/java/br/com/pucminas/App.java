package br.com.pucminas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
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
import br.com.pucminas.dtos.MicroserviceDTO;
import br.com.pucminas.dtos.ResultDTO;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

public class App {
    private static final String JAR_FILE_PATH = "src/test/resources/kanleitos-0.0.1-SNAPSHOT.jar";
    private static final String INCLUDED_PACKAGE = "br.com.kanleitos";
    public static final String REPOSITORY_NAME = "kanleitos-API";
    private static final String REPOSITORY_PATH_NAME = "target/repos/" + REPOSITORY_NAME;
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

        parameters.add(Arrays.asList("0", "0", "1", "10")); // Somente Histórico
        parameters.add(Arrays.asList("0", "0", "1", "20")); // Somente Histórico
        parameters.add(Arrays.asList("0", "0", "1", "30")); // Somente Histórico
        parameters.add(Arrays.asList("0", "0", "1", "40")); // Somente Histórico
        parameters.add(Arrays.asList("0", "0", "1", "50")); // Somente Histórico
        parameters.add(Arrays.asList("0", "0", "1", "60")); // Somente Histórico
        parameters.add(Arrays.asList("0", "0", "1", "70")); // Somente Histórico
        parameters.add(Arrays.asList("0", "0", "1", "80")); // Somente Histórico
        parameters.add(Arrays.asList("0", "0", "1", "90")); // Somente Histórico

        parameters.add(Arrays.asList("0", "1", "0", "10")); // Somente Método
        parameters.add(Arrays.asList("0", "1", "0", "20")); // Somente Método
        parameters.add(Arrays.asList("0", "1", "0", "30")); // Somente Método
        parameters.add(Arrays.asList("0", "1", "0", "40")); // Somente Método
        parameters.add(Arrays.asList("0", "1", "0", "50")); // Somente Método
        parameters.add(Arrays.asList("0", "1", "0", "60")); // Somente Método
        parameters.add(Arrays.asList("0", "1", "0", "70")); // Somente Método
        parameters.add(Arrays.asList("0", "1", "0", "80")); // Somente Método
        parameters.add(Arrays.asList("0", "1", "0", "90")); // Somente Método

        parameters.add(Arrays.asList("1", "0", "0", "10")); // Somente Classe
        parameters.add(Arrays.asList("1", "0", "0", "20")); // Somente Classe
        parameters.add(Arrays.asList("1", "0", "0", "30")); // Somente Classe
        parameters.add(Arrays.asList("1", "0", "0", "40")); // Somente Classe
        parameters.add(Arrays.asList("1", "0", "0", "50")); // Somente Classe
        parameters.add(Arrays.asList("1", "0", "0", "60")); // Somente Classe
        parameters.add(Arrays.asList("1", "0", "0", "70")); // Somente Classe
        parameters.add(Arrays.asList("1", "0", "0", "80")); // Somente Classe
        parameters.add(Arrays.asList("1", "0", "0", "90")); // Somente Classe

        parameters.add(Arrays.asList("1", "1", "0", "10")); // Classe e Método
        parameters.add(Arrays.asList("1", "1", "0", "20")); // Classe e Método
        parameters.add(Arrays.asList("1", "1", "0", "30")); // Classe e Método
        parameters.add(Arrays.asList("1", "1", "0", "40")); // Classe e Método
        parameters.add(Arrays.asList("1", "1", "0", "50")); // Classe e Método
        parameters.add(Arrays.asList("1", "1", "0", "60")); // Classe e Método
        parameters.add(Arrays.asList("1", "1", "0", "70")); // Classe e Método
        parameters.add(Arrays.asList("1", "1", "0", "80")); // Classe e Método
        parameters.add(Arrays.asList("1", "1", "0", "90")); // Classe e Método

        parameters.add(Arrays.asList("1", "0", "1", "10")); // Classe e Histórico
        parameters.add(Arrays.asList("1", "0", "1", "20")); // Classe e Histórico
        parameters.add(Arrays.asList("1", "0", "1", "30")); // Classe e Histórico
        parameters.add(Arrays.asList("1", "0", "1", "40")); // Classe e Histórico
        parameters.add(Arrays.asList("1", "0", "1", "50")); // Classe e Histórico
        parameters.add(Arrays.asList("1", "0", "1", "60")); // Classe e Histórico
        parameters.add(Arrays.asList("1", "0", "1", "70")); // Classe e Histórico
        parameters.add(Arrays.asList("1", "0", "1", "80")); // Classe e Histórico
        parameters.add(Arrays.asList("1", "0", "1", "90")); // Classe e Histórico

        parameters.add(Arrays.asList("0", "1", "1", "10")); // Método e Histórico
        parameters.add(Arrays.asList("0", "1", "1", "20")); // Método e Histórico
        parameters.add(Arrays.asList("0", "1", "1", "30")); // Método e Histórico
        parameters.add(Arrays.asList("0", "1", "1", "40")); // Método e Histórico
        parameters.add(Arrays.asList("0", "1", "1", "50")); // Método e Histórico
        parameters.add(Arrays.asList("0", "1", "1", "60")); // Método e Histórico
        parameters.add(Arrays.asList("0", "1", "1", "70")); // Método e Histórico
        parameters.add(Arrays.asList("0", "1", "1", "80")); // Método e Histórico
        parameters.add(Arrays.asList("0", "1", "1", "90")); // Método e Histórico

        parameters.add(Arrays.asList("1", "1", "1", "10")); // Todos os itens
        parameters.add(Arrays.asList("1", "1", "1", "20")); // Todos os itens
        parameters.add(Arrays.asList("1", "1", "1", "30")); // Todos os itens
        parameters.add(Arrays.asList("1", "1", "1", "40")); // Todos os itens
        parameters.add(Arrays.asList("1", "1", "1", "50")); // Todos os itens
        parameters.add(Arrays.asList("1", "1", "1", "60")); // Todos os itens
        parameters.add(Arrays.asList("1", "1", "1", "70")); // Todos os itens
        parameters.add(Arrays.asList("1", "1", "1", "80")); // Todos os itens
        parameters.add(Arrays.asList("1", "1", "1", "90")); // Todos os itens

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

                result.getMicroservices().forEach(microservice -> {
                    microservice.getServices()
                            .forEach(service -> printWriterAllItems.println(REPOSITORY_NAME + ";" + weightClassItem
                                    + ";" + weightMethodItem + ";" + weightHistoryItem + ";" + lowerLimitToGroup + ";"
                                    + result.getNumberMicroservices() + ";" + microservice.getName() + ";"
                                    + microservice.getNumberClasses() + ";" + microservice.getNumberMethods() + ";"
                                    + microservice.getNumberServices() + ";" + service.getFullMethodName()));
                });

                fillSilhouetteCoefficient(result, weightClassItem, weightMethodItem, weightHistoryItem,
                        lowerLimitToGroup);
            }
        }
    }

    private static void fillSilhouetteCoefficient(ResultDTO result, byte weightClassItem, byte weightMethodItem,
            byte weightHistoryItem, byte lowerLimitToGroup) throws IOException {

        try (FileWriter fileWriter = new FileWriter("silhouette-coefficient.log", true)) {
            try (PrintWriter printWriter = new PrintWriter(fileWriter, true)) {

                for (MicroserviceDTO microserviceAtual : result.getMicroservices()) {

                    for (Method serviceAtual : microserviceAtual.getServices()) {

                        // SameMicroservice - Calcular distância do serviço atual com os outros serviços
                        // no mesmo cluster (variábel a)
                        double meanDistanceSameMicroservice = getMeanDistance(serviceAtual,
                                microserviceAtual.getServices(), weightClassItem, weightMethodItem, weightHistoryItem,
                                lowerLimitToGroup);

                        // Calcular distância do serviço atual com os serviços de outros clusters
                        // (variábel b)
                        double meanDistanceNextCluster = Double.MAX_VALUE;
                        for (MicroserviceDTO nextMicroservice : result.getMicroservices()) {

                            // Ignorar cluster atual
                            if (!nextMicroservice.getName().equals(microserviceAtual.getName())) {

                                // Calcular distância
                                double meanDistanceNextClusterAux = getMeanDistance(serviceAtual,
                                        nextMicroservice.getServices(), weightClassItem, weightMethodItem,
                                        weightHistoryItem, lowerLimitToGroup);

                                // Para pegar a distância do cluster mais próximo ao atual
                                if (meanDistanceNextClusterAux < meanDistanceNextCluster)
                                    meanDistanceNextCluster = meanDistanceNextClusterAux;
                            }

                        }
                        // Calcular Silhouette Coefficient = (b - a) / max(a, b)
                        double silhouetteCoefficient = -100;
                        double max = Math.max(meanDistanceNextCluster, meanDistanceSameMicroservice);
                        if (max != 0) {
                            silhouetteCoefficient = (meanDistanceSameMicroservice - meanDistanceNextCluster) / max;
                        }
                        printWriter.println(REPOSITORY_NAME + ";" + weightClassItem + ";" + weightMethodItem + ";"
                                + weightHistoryItem + ";" + lowerLimitToGroup + ";" + microserviceAtual.getName() + ";"
                                + serviceAtual.getFullMethodName() + ";" + meanDistanceNextCluster + ";"
                                + meanDistanceSameMicroservice + ";" + silhouetteCoefficient);

                    }
                }
            }
        }
    }

    private static double getMeanDistance(Method serviceA, Set<Method> services, byte weightClassItem,
            byte weightMethodItem, byte weightHistoryItem, byte lowerLimitToGroup) {

        double partialDistanceSameMicroservice = 0;
        for (Method serviceB : services) {
            double similarity = calculateSimilarity(serviceA, serviceB, weightClassItem, weightMethodItem,
                    weightHistoryItem);

            if (similarity < lowerLimitToGroup)
                partialDistanceSameMicroservice += Math.pow(lowerLimitToGroup - similarity, 2);
        }

        return Math.sqrt(partialDistanceSameMicroservice);
    }

    private static double calculateSimilarity(Method a, Method b, byte weightClassItem, byte weightMethodItem,
            byte weightHistoryItem) {
        double numberEqualClasses = a.getClasses().stream().filter(className -> b.getClasses().contains(className))
                .count();
        double numberEqualMethods = a.getMethods().stream().filter(method -> b.getMethods().contains(method)).count();
        double numberEqualCommitIds = a.getCommitIds().stream().filter(commitId -> b.getCommitIds().contains(commitId))
                .count();
        double numberClasses = a.getClasses().size();
        double numberMethods = a.getMethods().size();
        double numberCommitIds = a.getCommitIds().size();

        double numerator = numberEqualClasses * weightClassItem + numberEqualMethods * weightMethodItem
                + numberEqualCommitIds * weightHistoryItem;
        double denominator = numberClasses * weightClassItem + numberMethods * weightMethodItem
                + numberCommitIds * weightHistoryItem;

        return denominator > 0 ? (numerator / denominator) * 100 : 0;
    }
}
