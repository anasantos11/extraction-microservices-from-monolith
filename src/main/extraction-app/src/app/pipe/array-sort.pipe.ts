import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "sort",
  pure: false,
})
export class ArraySortPipe implements PipeTransform {
  transform(array: any, field: string, reverse: boolean): any[] {
    if (!Array.isArray(array)) {
      return;
    }
    array.sort((a: any, b: any) => {
      if (a[field] < b[field]) {
        return -1;
      } else if (a[field] > b[field]) {
        return 1;
      } else {
        return 0;
      }
    });
    if (reverse) return array.reverse();
    else return array;
  }
}
