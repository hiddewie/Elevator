package com.hiddewieringa.elevator.projection

import com.hiddewieringa.elevator.domain.elevator.ElevatorCreated
import com.hiddewieringa.elevator.domain.elevator.ElevatorMovedToFloor
import com.hiddewieringa.elevator.domain.person.PersonEnteredElevator
import com.hiddewieringa.elevator.domain.person.PersonLeftElevator
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import com.hiddewieringa.elevator.projection.entity.elevator.ElevatorRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Service

@Service
open class ElevatorProjector {

    @EventHandler
    fun create(event: ElevatorCreated, elevatorRepository: ElevatorRepository) {
        elevatorRepository.save(Elevator(uuid = event.elevatorId.id, floor = 0, containsPerson = false))
    }

    @EventHandler
    open fun enter(event: PersonEnteredElevator, elevatorRepository: ElevatorRepository) {
        val elevators = elevatorRepository.findAll()
        elevators.forEach {
            it.containsPerson = true
            elevatorRepository.save(it)
        }
    }

    @EventHandler
    open fun leave(event: PersonLeftElevator, elevatorRepository: ElevatorRepository) {
        val elevators = elevatorRepository.findAll()
        elevators.forEach {
            it.containsPerson = false
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
}
