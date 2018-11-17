package com.hiddewieringa.elevator.projection

import com.hiddewieringa.elevator.domain.elevator.ElevatorCreated
import com.hiddewieringa.elevator.domain.person.PersonEnteredElevator
import com.hiddewieringa.elevator.domain.person.PersonLeftElevator
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import com.hiddewieringa.elevator.projection.entity.elevator.ElevatorRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class ElevatorProjector {

    @Autowired
    private lateinit var elevatorRepository: ElevatorRepository

    @EventHandler
    fun create(event: ElevatorCreated) {
        elevatorRepository.save(Elevator(event.elevatorId.id, false))
    }

    @EventHandler
    fun enter(event: PersonEnteredElevator) {
        val elevators = elevatorRepository.findAll()
        elevators.forEach {
            elevatorRepository.delete(it)
            elevatorRepository.save(Elevator(it.id, true))
        }
    }

    @EventHandler
    fun leave(event: PersonLeftElevator) {
        val elevators = elevatorRepository.findAll()
        elevators.forEach {
            elevatorRepository.delete(it)
            elevatorRepository.save(Elevator(it.id, false))
        }
    }
}
