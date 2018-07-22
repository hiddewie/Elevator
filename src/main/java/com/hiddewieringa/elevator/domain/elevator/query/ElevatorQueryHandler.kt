package com.hiddewieringa.elevator.domain.elevator.query

import com.hiddewieringa.elevator.domain.elevator.ElevatorCreated
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ElevatorQueryHandler {

    private val activeElevators = ArrayList<ElevatorId>()

    @EventHandler
    fun create(event: ElevatorCreated) {
        activeElevators.add(event.elevatorId)
    }

    open fun active(): List<ElevatorId> {
        return Collections.unmodifiableList(activeElevators)
    }
}
