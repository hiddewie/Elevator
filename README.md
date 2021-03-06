
# Elevator

[![Build Status](https://hiddewie.semaphoreci.com/badges/Elevator.svg)](https://hiddewie.semaphoreci.com/projects/Elevator)
[![Maintainability](https://api.codeclimate.com/v1/badges/641409c7c9336f1c0388/maintainability)](https://codeclimate.com/github/hiddewie/Elevator/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/641409c7c9336f1c0388/test_coverage)](https://codeclimate.com/github/hiddewie/Elevator/test_coverage)


This project models persons and elevators, and their interations. The goal is having a small project rich with domain logic, on which a CQRS architecture is well suited.

## Framework

Axon Framework version 4 is used for all Event Sourcing and CQRS related actions, in combination with Spring Boot 2 for easy HTTP interfacing.

## Getting started

#### Test

Run the command `./gradlew test`.

#### Run

Run the command `./gradlew bootRun`. Then web server will run on port 8080.

See [the frondend readme](web/README.md) for running the Angular frontend.

## Model

#### Elevator

- Can travel between floors, one at a time
- Can open and close doors
- Will automatically close the door if it has been opened for some time

#### Person

- Starts at a floor and wishes to travel to another floor
- Can enter and leave a elevator

### Interaction

A saga is used for interaction between the person and the elevator.
