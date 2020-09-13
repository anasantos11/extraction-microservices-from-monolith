package br.com.pucminas.dtos;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.pucminas.domain.ClassName;
import br.com.pucminas.domain.Method;

public class MicroserviceDTO {
    private Set<Method> services;
    private String name;

    public MicroserviceDTO(String name, Set<Method> services) {
        this.name = name;
        this.services = services;
    }

    public String getName() {
        return name;
    }

    public Set<Method> getServices() {
        return services;
    }

    public List<ClassName> getClasses() {
        return this.services.stream().map(Method::getClasses).flatMap(List::stream).distinct()
                .collect(Collectors.toList());
    }

    public List<Method> getMethods() {
        return this.services.stream().map(Method::getMethods).flatMap(List::stream).distinct()
                .collect(Collectors.toList());
    }

    public long getNumberServices() {
        return this.services.size();
    }

    public long getNumberClasses() {
        return getClasses().size();
    }

    public long getNumberMethods() {
        return getMethods().size();
    }
}