package br.com.pucminas.dtos;

import java.util.List;

public class ResultDTO {
    private List<MicroserviceDTO> microservices;
    private List<ItemRedundancyDTO> classRedundacies;
    private List<ItemRedundancyDTO> methodRedundancies;
    private double percentageMicrosserviceWithSameClass;
    private double percentageMicrosserviceWithSameMethods;

    public ResultDTO(List<MicroserviceDTO> microservices, List<ItemRedundancyDTO> classRedundacies,
            List<ItemRedundancyDTO> methodRedundancies, double percentageMicrosserviceWithSameClass,
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