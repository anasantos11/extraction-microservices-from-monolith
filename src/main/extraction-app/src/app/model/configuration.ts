export class Configuration {
  weightClassItem: number;
  weightMethodItem: number;
  weightHistoryItem: number;
  lowerLimitToGroup: number;
  gitRepositoryUri: string;
  repositoryUserName: string;
  repositoryPassword: string;
  isPrivateRepository: boolean;
  jarFilePath: string;
  includedPackages: string[] = new Array();
}
