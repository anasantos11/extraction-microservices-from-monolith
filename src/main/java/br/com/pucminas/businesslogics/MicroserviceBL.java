package br.com.pucminas.businesslogics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.pucminas.domain.ClassName;
import br.com.pucminas.domain.Method;
import br.com.pucminas.dtos.ItemRedundancyDTO;
import br.com.pucminas.dtos.MicroserviceDTO;
import br.com.pucminas.dtos.ResultDTO;

public class MicroserviceBL {
    private Map<String, Set<Method>> suggestedMicroservice;
    private List<MicroserviceDTO> microservices;

    public MicroserviceBL(Map<String, Set<Method>> suggestedMicroservice) {
        this.suggestedMicroservice = suggestedMicroservice;
        this.microservices = new ArrayList<>();
    }

    public ResultDTO generateResultsFromMicroservicesSuggestions() {
        this.suggestedMicroservice.entrySet()
                .forEach(entry -> this.microservices.add(new MicroserviceDTO(entry.getKey(), entry.getValue())));
        double numberMicroservices = this.microservices.size();
        List<ItemRedundancyDTO> classRedundacies = getClassRedundancies(numberMicroservices);
        List<ItemRedundancyDTO> methodRedundancies = getMethodRedundancies(numberMicroservices);

        return new ResultDTO(this.microservices, classRedundacies, methodRedundancies,
                getPercentageMicrosserviceWithSameClass(numberMicroservices),
                getPercentageMicrosserviceWithSameMethod(numberMicroservices));
    }

    private double getPercentageMicrosserviceWithSameClass(double numberMicroservices) {
        List<ClassName> classNames = this.microservices.stream().map(MicroserviceDTO::getClasses).flatMap(List::stream)
                .distinct().collect(Collectors.toList());
        double timesClassItemsAreUsed = classNames.stream()
                .mapToDouble(className -> getNumberMicroservicesContainsClass(className)).sum();

        return timesClassItemsAreUsed / (classNames.size() * numberMicroservices);
    }

    private double getPercentageMicrosserviceWithSameMethod(double numberMicroservices) {
        List<Method> methods = this.microservices.stream().map(MicroserviceDTO::getMethods).flatMap(List::stream)
                .distinct().collect(Collectors.toList());
        double timesMethodItemsAreUsed = methods.stream()
                .mapToDouble(method -> getNumberMicroservicesContainsMethod(method)).sum();

        return timesMethodItemsAreUsed / (methods.size() * numberMicroservices);
    }

    private List<ItemRedundancyDTO> getClassRedundancies(double numberMicroservices) {
        return this.microservices.stream().map(MicroserviceDTO::getClasses).flatMap(List::stream).distinct()
                .map(className -> new ItemRedundancyDTO(className.getName(),
                        getNumberMicroservicesContainsClass(className) / numberMicroservices))
                .collect(Collectors.toList());
    }

    private double getNumberMicroservicesContainsClass(ClassName className) {
        return this.microservices.stream().filter(microservice -> microservice.getClasses().contains(className))
                .count();
    }

    private List<ItemRedundancyDTO> getMethodRedundancies(double numberMicroservices) {

        return this.microservices.stream().map(MicroserviceDTO::getMethods).flatMap(List::stream).distinct()
                .map(method -> new ItemRedundancyDTO(method.getFullMethodName(),
                        getNumberMicroservicesContainsMethod(method) / numberMicroservices))
                .collect(Collectors.toList());
    }

    private double getNumberMicroservicesContainsMethod(Method method) {
        return this.microservices.stream().filter(microservice -> microservice.getMethods().contains(method)).count();
    }
}
