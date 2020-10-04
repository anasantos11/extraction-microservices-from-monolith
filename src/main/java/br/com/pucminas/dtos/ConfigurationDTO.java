package br.com.pucminas.dtos;

import java.util.List;

public class ConfigurationDTO {
    private byte weightClassItem;
    private byte weightMethodItem;
    private byte weightHistoryItem;
    private byte lowerLimitToGroup;
    private String gitRepositoryUri;
    private boolean isPrivateRepository;
    private String repositoryUserName;
    private String repositoryPassword;
    private String jarFilePath;
    private List<String> includedPackages;

    public byte getWeightClassItem() {
        return weightClassItem;
    }

    public List<String> getIncludedPackages() {
        return includedPackages;
    }

    public void setIncludedPackages(List<String> includedPackages) {
        this.includedPackages = includedPackages;
    }

    public String getJarFilePath() {
        return jarFilePath;
    }

    public void setJarFilePath(String jarFilePath) {
        this.jarFilePath = jarFilePath;
    }

    public String getGitRepositoryUri() {
        return gitRepositoryUri;
    }

    public void setGitRepositoryUri(String gitRepositoryUri) {
        this.gitRepositoryUri = gitRepositoryUri;
    }

    public String getRepositoryPassword() {
        return repositoryPassword;
    }

    public void setRepositoryPassword(String repositoryPassword) {
        this.repositoryPassword = repositoryPassword;
    }

    public String getRepositoryUserName() {
        return repositoryUserName;
    }

    public void setRepositoryUserName(String repositoryUserName) {
        this.repositoryUserName = repositoryUserName;
    }

    public boolean isPrivateRepository() {
        return isPrivateRepository;
    }

    public void setIsPrivateRepository(boolean isPrivateRepository) {
        this.isPrivateRepository = isPrivateRepository;
    }

    public byte getLowerLimitToGroup() {
        return lowerLimitToGroup;
    }

    public void setLowerLimitToGroup(byte lowerLimitToGroup) {
        this.lowerLimitToGroup = lowerLimitToGroup;
    }

    public byte getWeightHistoryItem() {
        return weightHistoryItem;
    }

    public void setWeightHistoryItem(byte weightHistoryItem) {
        this.weightHistoryItem = weightHistoryItem;
    }

    public byte getWeightMethodItem() {
        return weightMethodItem;
    }

    public void setWeightMethodItem(byte weightMethodItem) {
        this.weightMethodItem = weightMethodItem;
    }

    public void setWeightClassItem(byte weightClassItem) {
        this.weightClassItem = weightClassItem;
    }

}
