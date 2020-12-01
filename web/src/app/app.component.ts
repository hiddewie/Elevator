import {Component, EventEmitter, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {interval, Observable, of} from "rxjs";
import {catchError, map, switchMap} from "rxjs/operators";

interface Elevator {
  uuid: string;
  floor: string;
  numberOfPersons: number;
  numberOfTargets: number;
  doorsOpened: boolean;
}

interface Person {
  uuid: string;
  inElevator: boolean;
  initialFloor: number;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  readonly floors = [...Array(10 + 1).keys()].reverse();

  liveStatus$: Observable<string>;
  readyStatus$: Observable<string>;
  elevators$: Observable<Elevator[]>;
  persons$: Observable<Person[]>;

  private pollPeriod = 1000;
  private serviceUrl = "http://localhost:8080";

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.elevators$ = this.registerEventSource(`${this.serviceUrl}/elevator`);
    this.persons$ = this.registerEventSource(`${this.serviceUrl}/person`);

    this.liveStatus$ = interval(this.pollPeriod)
      .pipe(switchMap(() => this.http.get(`${this.serviceUrl}/status/live`, {responseType: 'text'}).pipe(
        map(() => 'OK'),
        catchError(() => of('ERROR')),
      )));

    this.readyStatus$ = interval(this.pollPeriod)
      .pipe(switchMap(() => this.http.get(`${this.serviceUrl}/status/ready`, {responseType: 'text'}).pipe(
        map(() => 'OK'),
        catchError(() => of('ERROR')),
      )));
  }

  addElevator() {
    this.http.get(`${this.serviceUrl}/elevator/create`)
      .subscribe(uuid => console.info('Done', uuid))
  }

  personArrives(floor: number, target: number) {
    this.http.get(`${this.serviceUrl}/person/arrive/${floor}/${target}`)
      .subscribe(uuid => console.info('Done', uuid))
  }

  private registerEventSource<T>(url: string): Observable<T> {
    const emitter = new EventEmitter<T>();
    const source = new EventSource(url);
    source.addEventListener('message', message => {
      emitter.emit(JSON.parse(message.data));
    });
    source.addEventListener('error', error => {
      emitter.error(error);
      emitter.complete();
    });

    return emitter;
  }
}
