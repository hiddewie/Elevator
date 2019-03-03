package com.hiddewieringa.elevator.projection

import com.hiddewieringa.elevator.domain.elevator.ElevatorCreated
import com.hiddewieringa.elevator.domain.elevator.ElevatorMovedToFloor
import com.hiddewieringa.elevator.domain.elevator.ElevatorTargetAssigned
import com.hiddewieringa.elevator.domain.elevator.ElevatorTargetRemoved
import com.hiddewieringa.elevator.domain.elevator.query.ActiveQuery
import com.hiddewieringa.elevator.domain.elevator.query.FloorResult
import com.hiddewieringa.elevator.domain.elevator.query.QueryFloor
import com.hiddewieringa.elevator.domain.person.PersonEnteredElevator
import com.hiddewieringa.elevator.domain.person.PersonLeftElevator
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import com.hiddewieringa.elevator.projection.entity.elevator.ElevatorRepository
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service

@Service
open class ElevatorProjector {

    @EventHandler
    fun create(event: ElevatorCreated, elevatorRepository: ElevatorRepository) {
        elevatorRepository.save(
            Elevator(
                uuid = event.elevatorId.id,
                group = event.groupId.id,
                floor = 0,
                numberOfPersons = 0,
                numberOfTargets = 0
            )
        )
    }

    @EventHandler
    open fun enter(event: PersonEnteredElevator, elevatorRepository: ElevatorRepository) {
        val elevators = elevatorRepository.findAll()
        elevators.forEach {
            it.numberOfPersons++
            elevatorRepository.save(it)
        }
    }

    @EventHandler
    open fun leave(event: PersonLeftElevator, elevatorRepository: ElevatorRepository) {
        val elevators = elevatorRepository.findAll()
        elevators.forEach {
            it.numberOfPersons--
            elevatorRepository.save(it)
        }
    }

    @EventHandler
    open fun floor(event: ElevatorMovedToFloor, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.floor = event.floor
            elevatorRepository.save(it)
        }
    }

    @EventHandler
    open fun floor(event: ElevatorTargetAssigned, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfTargets++
            elevatorRepository.save(it)
        }
    }

    @EventHandler
    open fun floor(event: ElevatorTargetRemoved, elevatorRepository: ElevatorRepository) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfTargets--
            elevatorRepository.save(it)
        }
    }

    @QueryHandler
    open fun active(query: ActiveQuery, elevatorRepository: ElevatorRepository) =
        elevatorRepository.findAll()

    @QueryHandler
    open fun floor(query: QueryFloor, elevatorRepository: ElevatorRepository) =
        elevatorRepository
            .findByUuid(query.elevatorId.id)
            .map(Elevator::floor).orElse(null)
            .let(::FloorResult)
}
