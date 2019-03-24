import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {interval, Observable} from "rxjs";
import {catchError, map, switchMap} from "rxjs/operators";
import {of} from "rxjs/internal/observable/of";

interface Elevator {
  uuid: string;
  floor: string;
  numberOfPersons: number;
  numberOfTargets: number;
  doorsOpened: boolean;
}

interface Person {
  uuid: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'web';

  liveStatus$: Observable<string>;
  readyStatus$: Observable<string>;
  elevators$: Observable<Elevator[]>;
  // persons$: Observable<Person>;

  private pollPeriod = 1000;
  private serviceUrl = "http://localhost:8080";

  constructor(
    private http: HttpClient
  ) {
    this.elevators$ = interval(this.pollPeriod)
      .pipe(
        switchMap(() => http.get<Elevator[]>(`${this.serviceUrl}/elevator`)),
        catchError(() => of([]))
      );

    this.liveStatus$ = interval(this.pollPeriod)
      .pipe(
        switchMap(() => http.get(`${this.serviceUrl}/status/live`)),
        map(() => 'OK'),
        catchError(() => of('ERROR'))
      );

    this.readyStatus$ = interval(this.pollPeriod)
      .pipe(
        switchMap(() => http.get(`${this.serviceUrl}/status/ready`)),
        map(() => 'OK'),
        catchError(() => of('ERROR'))
      );
  }

  addElevator() {
    this.http.get(`${this.serviceUrl}/elevator/create`)
      .subscribe(uuid => console.info('Done', uuid))
  }

  personArrives() {
    this.http.get(`${this.serviceUrl}/person/arrive/1/2`)
      .subscribe(uuid => console.info('Done', uuid))
  }
}
