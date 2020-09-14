import { ClassName } from "./class-name";
import { Method } from "./method";
import { Service } from "./service";

export class Microservice {
  services: Service[];
  name: string;
  classes: ClassName[];
  methods: Method[];
  numberServices: number;
  numberMethods: number;
  numberClasses: number;
}
