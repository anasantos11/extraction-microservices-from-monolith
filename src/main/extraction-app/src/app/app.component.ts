import { Component } from "@angular/core";
import { Configuration } from "./model/configuration";
import { ResultSuggestion } from "./model/result-suggestion";
import { MicroserviceService } from "./service/microservice.service";
import { COMMA, ENTER } from "@angular/cdk/keycodes";
import { MatChipInputEvent } from "@angular/material/chips";
import { Microservice } from "./model/microservice";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent {
  title = "extraction-app";
  configuration: Configuration;
  result: ResultSuggestion;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];

  constructor(private microserviceService: MicroserviceService) {
    this.configuration = new Configuration();
  }

  addPackage(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;
    if ((value || "").trim()) {
      this.configuration.includedPackages.push(value.trim());
    }

    if (input) {
      input.value = "";
    }
  }

  removePackage(packageName: string): void {
    const index = this.configuration.includedPackages.indexOf(packageName);

    if (index >= 0) {
      this.configuration.includedPackages.splice(index, 1);
    }
  }

  generateMicroserviceSuggestions() {
    this.result = null;
    this.microserviceService
      .generateMicroserviceSuggestions(this.configuration)
      .subscribe(
        (response) => {
          this.result = response;
        },
        (errorResponse) => {
          alert("Message: " + errorResponse.message);
        }
      );
  }

  getNameServices(microservice: Microservice): string {
    return microservice.services
      .map((service) => service.fullMethodName)
      .join("; ");
  }
}
