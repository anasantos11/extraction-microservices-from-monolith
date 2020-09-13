package br.com.pucminas.dtos;

import java.util.List;

public class ResultDTO {
    private List<MicroserviceDTO> microservices;
    private List<ItemRedundancyDTO> classRedundacies;
    private List<ItemRedundancyDTO> methodRedundancies;

    public ResultDTO(List<MicroserviceDTO> microservices, List<ItemRedundancyDTO> classRedundacies,
            List<ItemRedundancyDTO> methodRedundancies) {
        this.microservices = microservices;
        this.classRedundacies = classRedundacies;
        this.methodRedundancies = methodRedundancies;
    }

    public List<ItemRedundancyDTO> getMethodRedundancies() {
        return methodRedundancies;
    }

    public List<ItemRedundancyDTO> getClassRedundacies() {
        return classRedundacies;
    }

    public List<MicroserviceDTO> getMicroservices() {
        return microservices;
    }

    public long getNumberMicroservices() {
        return this.microservices.size();
    }

}