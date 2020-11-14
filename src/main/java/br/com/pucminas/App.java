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

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import br.com.pucminas.businesslogics.MicroserviceBL;
import br.com.pucminas.businesslogics.ServiceBL;
import br.com.pucminas.domain.Method;
import br.com.pucminas.dtos.ConfigurationDTO;
import br.com.pucminas.dtos.MicroserviceDTO;
import br.com.pucminas.dtos.ResultDTO;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

public class App {
    public static String REPOSITORY_NAME;

    public static void main(String... args) {

        List<ConfigurationDTO> applications = new ArrayList<>();
        var kanleitos = new ConfigurationDTO();
        kanleitos.setGitRepositoryUri("https://github.com/anasantos11/kanleitos-API.git");
        kanleitos.setIsPrivateRepository(false);
        kanleitos.setJarFilePath("C:/Users/anasa/Desktop/TCC ANÁLISE/Arquivos JAR/kanleitos-0.0.1-SNAPSHOT.jar");
        kanleitos.setIncludedPackages(Arrays.asList("br.com.kanleitos"));
        applications.add(kanleitos);

        var monolithEverest = new ConfigurationDTO();
        monolithEverest.setGitRepositoryUri("https://github.com/arun-gupta/microservices.git");
        monolithEverest.setIsPrivateRepository(false);
        monolithEverest.setJarFilePath("C:/Users/anasa/Desktop/TCC ANÁLISE/Arquivos JAR/monolith-everest.war");
        monolithEverest.setIncludedPackages(Arrays.asList("org.javaee7.wildfly.samples.everest"));
        applications.add(monolithEverest);

        var snowman = new ConfigurationDTO();
        snowman.setGitRepositoryUri("https://github.com/colinbut/monolith-enterprise-application.git");
        snowman.setIsPrivateRepository(false);
        snowman.setJarFilePath("C:/Users/anasa/Desktop/TCC ANÁLISE/Arquivos JAR/snowman.jar");
        snowman.setIncludedPackages(Arrays.asList("com.mycompany.entapp.snowman"));
        applications.add(snowman);

        applications.forEach(application -> {
            try {
                String[] auxRepositoryName = application.getGitRepositoryUri().split("/");
                REPOSITORY_NAME = auxRepositoryName[auxRepositoryName.length - 1];
                run(application);
            } catch (GitAPIException | IOException e) {
                System.out.println("Erro para gerar sugestões na aplicação + " + application.getGitRepositoryUri());
                e.printStackTrace();
            }
        });
    }

    public static void run(ConfigurationDTO configuration) throws GitAPIException, IOException {
        File repositoryFolder = new File("target/repos/" + REPOSITORY_NAME);
        File jarFile = new File(configuration.getJarFilePath());

        Git git = null;
        try {
            git = Git.open(repositoryFolder);
        } catch (IOException ioException) {
            CloneCommand cloneCommand = Git.cloneRepository().setURI(configuration.getGitRepositoryUri())
                    .setDirectory(repositoryFolder);

            if (configuration.isPrivateRepository())
                cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                        configuration.getRepositoryUserName(), configuration.getRepositoryPassword()));

            git = cloneCommand.call();

        }

        GitRepository gitRepository = new GitRepository(git);
        CallGraphRepository callGraphRepository = new CallGraphRepository(jarFile, configuration.getIncludedPackages());

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

        for (List<String> list : parameters) {
            configuration.setWeightClassItem(Byte.parseByte(list.get(0)));
            configuration.setWeightMethodItem(Byte.parseByte(list.get(1)));
            configuration.setWeightHistoryItem(Byte.parseByte(list.get(2)));
            configuration.setLowerLimitToGroup(Byte.parseByte(list.get(3)));

            generateSuggestions(gitRepository, callGraphRepository, configuration);
        }
    }

    private static void generateSuggestions(GitRepository gitRepository, CallGraphRepository callGraphRepository,
            ConfigurationDTO configuration) throws IOException, GitAPIException {

        try (FileWriter fileWriterAllItems = new FileWriter("microservices.log", true)) {
            try (PrintWriter printWriterAllItems = new PrintWriter(fileWriterAllItems, true)) {

                Map<String, Set<Method>> microservices = new ServiceBL(gitRepository, callGraphRepository,
                        configuration.getWeightClassItem(), configuration.getWeightMethodItem(),
                        configuration.getWeightHistoryItem(), configuration.getLowerLimitToGroup())
                                .groupServicesInMicroservices();

                ResultDTO result = new MicroserviceBL(microservices).generateResultsFromMicroservicesSuggestions();

                try (FileWriter fileRedundancy = new FileWriter("microservices-redundancy.log", true)) {
                    try (PrintWriter printerRedundancy = new PrintWriter(fileRedundancy, true)) {

                        printerRedundancy.println(REPOSITORY_NAME + ";" + configuration.getWeightClassItem() + ";"
                                + configuration.getWeightMethodItem() + ";" + configuration.getWeightHistoryItem() + ";"
                                + configuration.getLowerLimitToGroup() + ";" + result.getNumberMicroservices() + ";"
                                + result.getPercentageMicrosserviceWithSameClass() + ";"
                                + result.getPercentageMicrosserviceWithSameMethods());
                    }
                }

                try (FileWriter fileRedundancy = new FileWriter("items-redundancy.log", true)) {
                    try (PrintWriter printerRedundancy = new PrintWriter(fileRedundancy, true)) {

                        result.getClassRedundacies()
                                .forEach(classRedundancy -> printerRedundancy.println(REPOSITORY_NAME + ";"
                                        + configuration.getWeightClassItem() + ";" + configuration.getWeightMethodItem()
                                        + ";" + configuration.getWeightHistoryItem() + ";"
                                        + configuration.getLowerLimitToGroup() + ";" + result.getNumberMicroservices()
                                        + ";" + "ClassRedundancy" + ";" + classRedundancy.getName() + ";"
                                        + classRedundancy.getPercentageRedundancy()));

                        result.getMethodRedundancies()
                                .forEach(methodRedundancy -> printerRedundancy.println(REPOSITORY_NAME + ";"
                                        + configuration.getWeightClassItem() + ";" + configuration.getWeightMethodItem()
                                        + ";" + configuration.getWeightHistoryItem() + ";"
                                        + configuration.getLowerLimitToGroup() + ";" + result.getNumberMicroservices()
                                        + ";" + "MethodRedundancy" + ";" + methodRedundancy.getName() + ";"
                                        + methodRedundancy.getPercentageRedundancy()));

                        result.getMicroservices().forEach(microservice -> microservice.getServices()
                                .forEach(service -> printWriterAllItems.println(REPOSITORY_NAME + ";"
                                        + configuration.getWeightClassItem() + ";" + configuration.getWeightMethodItem()
                                        + ";" + configuration.getWeightHistoryItem() + ";"
                                        + configuration.getLowerLimitToGroup() + ";" + result.getNumberMicroservices()
                                        + ";" + microservice.getName() + ";" + microservice.getNumberClasses() + ";"
                                        + microservice.getNumberMethods() + ";" + microservice.getNumberServices() + ";"
                                        + service.getFullMethodName())));
                    }
                }

                fillSilhouetteCoefficient(result, configuration);
            }
        }
    }

    private static void fillSilhouetteCoefficient(ResultDTO result, ConfigurationDTO configuration) throws IOException {

        try (FileWriter fileWriter = new FileWriter("silhouette-coefficient.log", true)) {
            try (PrintWriter printWriter = new PrintWriter(fileWriter, true)) {

                for (MicroserviceDTO microserviceAtual : result.getMicroservices()) {

                    for (Method serviceAtual : microserviceAtual.getServices()) {

                        // SameMicroservice - Calcular distância do serviço atual com os outros serviços
                        // no mesmo cluster (variábel a)
                        double meanDistanceSameMicroservice = getMeanDistance(serviceAtual,
                                microserviceAtual.getServices(), configuration);

                        // Calcular distância do serviço atual com os serviços de outros clusters
                        // (variábel b)
                        double meanDistanceNextCluster = getMeanDistanceNextCluster(result, microserviceAtual,
                                serviceAtual, configuration);

                        // Calcular Silhouette Coefficient = (b - a) / max(a, b)
                        double silhouetteCoefficient = getSilhouetteCoefficient(meanDistanceSameMicroservice,
                                meanDistanceNextCluster);

                        printWriter.println(REPOSITORY_NAME + ";" + configuration.getWeightClassItem() + ";"
                                + configuration.getWeightMethodItem() + ";" + configuration.getWeightHistoryItem() + ";"
                                + configuration.getLowerLimitToGroup() + ";" + microserviceAtual.getName() + ";"
                                + serviceAtual.getFullMethodName() + ";" + meanDistanceSameMicroservice + ";"
                                + meanDistanceNextCluster + ";" + silhouetteCoefficient);

                    }
                }
            }
        }
    }

    private static double getMeanDistanceNextCluster(ResultDTO result, MicroserviceDTO microserviceAtual,
            Method serviceAtual, ConfigurationDTO configuration) {
        double meanDistanceNextCluster = Double.MAX_VALUE;
        for (MicroserviceDTO nextMicroservice : result.getMicroservices()) {

            // Ignorar cluster atual
            if (!nextMicroservice.getName().equals(microserviceAtual.getName())) {

                // Calcular distância
                double meanDistanceNextClusterAux = getMeanDistance(serviceAtual, nextMicroservice.getServices(),
                        configuration);

                // Para pegar a distância do cluster mais próximo ao atual
                if (meanDistanceNextClusterAux < meanDistanceNextCluster)
                    meanDistanceNextCluster = meanDistanceNextClusterAux;
            }
        }

        return meanDistanceNextCluster;
    }

    private static double getSilhouetteCoefficient(double meanDistanceSameMicroservice,
            double meanDistanceNextCluster) {
        double silhouetteCoefficient = -100;
        double max = Math.max(meanDistanceNextCluster, meanDistanceSameMicroservice);
        if (max != 0) {
            silhouetteCoefficient = (meanDistanceNextCluster - meanDistanceSameMicroservice) / max;
        }

        return silhouetteCoefficient;
    }

    private static double getMeanDistance(Method serviceA, Set<Method> services, ConfigurationDTO configuration) {

        double partialDistanceSameMicroservice = 0;
        for (Method serviceB : services) {
            double similarity = calculateSimilarity(serviceA, serviceB, configuration.getWeightClassItem(),
                    configuration.getWeightMethodItem(), configuration.getWeightHistoryItem());

            if (similarity < configuration.getLowerLimitToGroup())
                partialDistanceSameMicroservice += Math.pow(configuration.getLowerLimitToGroup() - similarity, 2);
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
