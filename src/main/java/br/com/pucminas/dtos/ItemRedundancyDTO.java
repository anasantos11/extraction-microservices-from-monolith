package br.com.pucminas.dtos;

public class ItemRedundancyDTO {
    private String name;
    private double percentageRedundancy;

    public ItemRedundancyDTO(String name, double percentageRedundancy) {
        this.name = name;
        this.percentageRedundancy = percentageRedundancy;
    }

    public ItemRedundancyDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getPercentageRedundancy() {
        return percentageRedundancy;
    }
}