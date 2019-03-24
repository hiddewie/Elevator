package com.hiddewieringa.elevator.projection

import com.hiddewieringa.elevator.domain.elevator.*
import com.hiddewieringa.elevator.domain.elevator.query.ActiveQuery
import com.hiddewieringa.elevator.domain.elevator.query.FloorResult
import com.hiddewieringa.elevator.domain.elevator.query.QueryFloor
import com.hiddewieringa.elevator.domain.person.PersonEnteredElevator
import com.hiddewieringa.elevator.domain.person.PersonLeftElevator
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import com.hiddewieringa.elevator.projection.entity.elevator.ElevatorRepository
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service

@Service
class ElevatorProjector {

    @EventSourcingHandler
    fun create(event: ElevatorCreated, elevatorRepository: ElevatorRepository) {
        elevatorRepository.save(
            Elevator(
                uuid = event.elevatorId.id,
                group = event.groupId.id,
                floor = 0,
                numberOfPersons = 0,
                numberOfTargets = 0,
                doorsOpened = false
            )
        )
    }

    @EventSourcingHandler
    fun enter(event: PersonEnteredElevator, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfPersons++
            elevatorRepository.save(it)
        }
    }

    @EventSourcingHandler
    fun leave(event: PersonLeftElevator, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfPersons--
            elevatorRepository.save(it)
        }
    }

    @EventSourcingHandler
    fun floor(event: ElevatorMovedToFloor, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.floor = event.floor
            elevatorRepository.save(it)
        }
    }

    @EventSourcingHandler
    fun floor(event: ElevatorTargetAssigned, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfTargets++
            elevatorRepository.save(it)
        }
    }

    @EventSourcingHandler
    fun floor(event: ElevatorTargetRemoved, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfTargets--
            elevatorRepository.save(it)
        }
    }

    @EventSourcingHandler
    fun floor(event: ElevatorDoorsOpened, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.doorsOpened = true
            elevatorRepository.save(it)
        }
    }

    @EventSourcingHandler
    fun floor(event: ElevatorDoorsClosed, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.doorsOpened = false
            elevatorRepository.save(it)
        }
    }

    @QueryHandler
    fun active(query: ActiveQuery, elevatorRepository: ElevatorRepository) =
        elevatorRepository.findAll()

    @QueryHandler
    fun floor(query: QueryFloor, elevatorRepository: ElevatorRepository) =
        elevatorRepository
            .findByUuid(query.elevatorId.id)
            .map(Elevator::floor).orElse(null)
            .let(::FloorResult)
}
