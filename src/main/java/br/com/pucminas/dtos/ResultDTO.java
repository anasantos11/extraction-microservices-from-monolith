package br.com.pucminas.dtos;

import java.util.List;
import java.util.Set;

public class ResultDTO {
    private List<MicroserviceDTO> microservices;
    private Set<ItemRedundancyDTO> classRedundacies;
    private Set<ItemRedundancyDTO> methodRedundancies;
    private double percentageMicrosserviceWithSameClass;
    private double percentageMicrosserviceWithSameMethods;

    public ResultDTO(List<MicroserviceDTO> microservices, Set<ItemRedundancyDTO> classRedundacies,
            Set<ItemRedundancyDTO> methodRedundancies, double percentageMicrosserviceWithSameClass,
            double percentageMicrosserviceWithSameMethods) {
        this.microservices = microservices;
        this.classRedundacies = classRedundacies;
        this.methodRedundancies = methodRedundancies;
        this.percentageMicrosserviceWithSameClass = percentageMicrosserviceWithSameClass;
        this.percentageMicrosserviceWithSameMethods = percentageMicrosserviceWithSameMethods;
    }

    public double getPercentageMicrosserviceWithSameMethods() {
        return percentageMicrosserviceWithSameMethods;
    }

    public double getPercentageMicrosserviceWithSameClass() {
        return percentageMicrosserviceWithSameClass;
    }

    public Set<ItemRedundancyDTO> getMethodRedundancies() {
        return methodRedundancies;
    }

    public Set<ItemRedundancyDTO> getClassRedundacies() {
        return classRedundacies;
    }

    public List<MicroserviceDTO> getMicroservices() {
        return microservices;
    }

    public long getNumberMicroservices() {
        return this.microservices.size();
    }

}