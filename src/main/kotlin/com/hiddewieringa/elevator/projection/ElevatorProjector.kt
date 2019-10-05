package com.hiddewieringa.elevator.projection

import com.hiddewieringa.elevator.domain.elevator.*
import com.hiddewieringa.elevator.domain.elevator.ActiveQuery
import com.hiddewieringa.elevator.domain.elevator.FloorResult
import com.hiddewieringa.elevator.domain.elevator.QueryFloor
import com.hiddewieringa.elevator.domain.person.PersonEnteredElevator
import com.hiddewieringa.elevator.domain.person.PersonLeftElevator
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import com.hiddewieringa.elevator.projection.entity.elevator.ElevatorRepository
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ElevatorProjector @Autowired constructor(
    private val queryUpdateEmitter: QueryUpdateEmitter,
    private val elevatorRepository: ElevatorRepository
) {

    @EventSourcingHandler
    fun create(event: ElevatorCreated) {
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
        updateQueryResults()
    }

    @EventSourcingHandler
    fun enter(event: PersonEnteredElevator) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfPersons++
            elevatorRepository.save(it)
        }
        updateQueryResults()
    }

    @EventSourcingHandler
    fun leave(event: PersonLeftElevator) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfPersons--
            elevatorRepository.save(it)
        }
        updateQueryResults()
    }

    @EventSourcingHandler
    fun floor(event: ElevatorMovedToFloor) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.floor = event.floor
            elevatorRepository.save(it)
        }
        updateQueryResults()
    }

    @EventSourcingHandler
    fun floor(event: ElevatorTargetAssigned) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfTargets++
            elevatorRepository.save(it)
        }
        updateQueryResults()
    }

    @EventSourcingHandler
    fun floor(event: ElevatorTargetRemoved) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.numberOfTargets--
            elevatorRepository.save(it)
        }
        updateQueryResults()
    }

    @EventSourcingHandler
    fun floor(event: ElevatorDoorsOpened) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.doorsOpened = true
            elevatorRepository.save(it)
        }
        updateQueryResults()
    }

    @EventSourcingHandler
    fun floor(event: ElevatorDoorsClosed) {
        elevatorRepository.findByUuid(event.elevatorId.id).ifPresent {
            it.doorsOpened = false
            elevatorRepository.save(it)
        }
        updateQueryResults()
    }

    @QueryHandler
    fun active(query: ActiveQuery) =
        elevatorRepository.findAll()

    @QueryHandler
    fun floor(query: QueryFloor) =
        elevatorRepository
            .findByUuid(query.elevatorId.id)
            .map(Elevator::floor).orElse(null)
            .let(::FloorResult)

    private fun updateQueryResults() {
        queryUpdateEmitter.emit(
            ActiveQuery::class.java,
            { true },
            elevatorRepository.findAll()
        )
    }
}
