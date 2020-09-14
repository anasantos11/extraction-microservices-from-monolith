import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Configuration } from "../model/configuration";
import { Observable } from "rxjs";
import { ResultSuggestion } from "../model/result-suggestion";

@Injectable({
  providedIn: "root",
})
export class MicroserviceService {
  baseUrl: string = "http://localhost:8000/api/microservice/";

  constructor(private http: HttpClient) {}
  generateMicroserviceSuggestions(
    configuration: Configuration
  ): Observable<ResultSuggestion> {
    return this.http.post<ResultSuggestion>(this.baseUrl, configuration);
  }
}
