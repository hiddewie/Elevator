
# Elevator

This project models persons and elevators, and their interations. The goal is having a small project rich with domain logic, on which a CQRS architecture is well suited.

## Framework

Axon Framework version 3 is used for all Event Sourcing and CQRS related actions, in combination with Spring Boot 2 for easy HTTP interfacing.

## Model

#### Elevator

- Can travel between floors, one at a time
- Can open and close doors

#### Person

- Starts at a floor and wishes to travel to another floor
- Can enter and leave a elevator

### Interaction

A saga is used for interaction between the person and the elevator.
