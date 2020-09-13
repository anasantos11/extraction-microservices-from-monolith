import { ItemRedundancy } from './item-redundancy';
import { Microservice } from './microservice';

export class ResultSuggestion {
    microservices: Microservice[];
    classRedundancies: ItemRedundancy[];
    numberMicroservices: number;
    methodRedundancies: ItemRedundancy[];
}
