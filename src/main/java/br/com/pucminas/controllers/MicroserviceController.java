package br.com.pucminas.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.pucminas.businesslogics.MicroserviceBL;
import br.com.pucminas.businesslogics.ServiceBL;
import br.com.pucminas.domain.Method;
import br.com.pucminas.dtos.ConfigurationDTO;
import br.com.pucminas.dtos.ResultDTO;
import br.com.pucminas.repositories.CallGraphRepository;
import br.com.pucminas.repositories.GitRepository;

@Controller
@RequestMapping("/api/microservice")
public class MicroserviceController {

    @PostMapping("/")
    public @ResponseBody ResponseEntity<ResultDTO> generateMicroserviceSuggestions(
            @RequestBody ConfigurationDTO configuration) throws IOException, GitAPIException {

        File tempRepositoryDirectory = Files.createTempDirectory("extraction-repositories").toFile();
        File jarFile = new File(configuration.getJarFilePath());

        Git git = null;
        try {
            git = Git.open(tempRepositoryDirectory);
        } catch (IOException ioException) {
            git = Git.cloneRepository().setURI(configuration.getGitRepositoryUri())
                    .setDirectory(tempRepositoryDirectory).call();
        }

        GitRepository gitRepository = new GitRepository(git);
        CallGraphRepository callGraphRepository = new CallGraphRepository(jarFile, configuration.getIncludedPackages());

        Map<String, Set<Method>> microservices = new ServiceBL(gitRepository, callGraphRepository,
                configuration.getWeightClassItem(), configuration.getWeightMethodItem(),
                configuration.getWeightHistoryItem(), configuration.getLowerLimitToGroup())
                        .groupServicesInMicroservices();

        ResultDTO result = new MicroserviceBL(microservices).generateResultsFromMicroservicesSuggestions();
        
        return ResponseEntity.ok(result);
    }
}
