import {Component, EventEmitter, OnInit} from '@angular/core';
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
export class AppComponent implements OnInit {

  title = 'web';

  liveStatus$: Observable<string>;
  readyStatus$: Observable<string>;
  elevators$: Observable<Elevator[]>;
  persons$: Observable<Person[]>;

  private pollPeriod = 1000;
  private serviceUrl = "http://localhost:8080";

  constructor(private http: HttpClient) {
  }

  addElevator() {
    this.http.get(`${this.serviceUrl}/elevator/create`)
      .subscribe(uuid => console.info('Done', uuid))
  }

  personArrives() {
    this.http.get(`${this.serviceUrl}/person/arrive/1/2`)
      .subscribe(uuid => console.info('Done', uuid))
  }

  ngOnInit(): void {
    this.elevators$ = this.registerEventSource(`${this.serviceUrl}/elevator`);
    this.persons$ = this.registerEventSource(`${this.serviceUrl}/person`);

    this.liveStatus$ = interval(this.pollPeriod)
      .pipe(
        switchMap(() => this.http.get(`${this.serviceUrl}/status/live`)),
        map(() => 'OK'),
        catchError(() => of('ERROR'))
      );

    this.readyStatus$ = interval(this.pollPeriod)
      .pipe(
        switchMap(() => this.http.get(`${this.serviceUrl}/status/ready`)),
        map(() => 'OK'),
        catchError(() => of('ERROR'))
      );
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
