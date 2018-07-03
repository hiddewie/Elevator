package com.hiddewieringa.elevator.domain.person.model

import com.hiddewieringa.elevator.domain.elevator.*
import com.hiddewieringa.elevator.domain.person.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventhandling.EventHandler
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class PersonAggregate {

    @AggregateIdentifier
    private var id: PersonId? = null

    private var floor = 0L;
    private var requestedFloor = 0L;

    private var inElevator = false;

    private constructor()

    @CommandHandler
    constructor(command: PersonArrives) {
        apply(PersonArrived(command.personId, command.floor, command.requestedFloor))
    }

    @EventHandler
    fun personArrived(event: PersonArrived) {
        this.id = event.personId
        this.floor = event.floor
        this.requestedFloor = event.requestedFloor
    }

    @CommandHandler
    fun enterElevator(command: EnterElevator) {
        if (inElevator) {
            throw IllegalArgumentException("Already in elevator")
        }
        apply(PersonEnteredElevator(command.personId))
    }

    @EventHandler
    fun elevatorEntered(event: PersonEnteredElevator) {
        inElevator = true
    }

    @CommandHandler
    fun leaveElevator(command: LeaveElevator) {
        if (!inElevator) {
            throw IllegalArgumentException("Not in elevator")
        }
        apply(PersonLeftElevator(command.personId))
    }

    @EventHandler
    fun elevatorLeft(event: PersonLeftElevator) {
        inElevator = false
    }

    @EventHandler
    fun arrivedAtFloor(event: PersonArrivedAtFloor) {
        floor = event.floor
    }
}
