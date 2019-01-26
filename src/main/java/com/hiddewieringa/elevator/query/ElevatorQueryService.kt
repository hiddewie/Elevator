package com.hiddewieringa.elevator.query

import com.hiddewieringa.elevator.domain.elevator.query.ActiveQuery
import com.hiddewieringa.elevator.domain.elevator.query.FloorResult
import com.hiddewieringa.elevator.domain.elevator.query.QueryFloor
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import com.hiddewieringa.elevator.projection.entity.elevator.ElevatorRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ElevatorQueryService(var repository: ElevatorRepository) {

    @QueryHandler
    open fun active(query: ActiveQuery): List<Elevator> {
        return repository.findAll().toList()
    }

    @QueryHandler
    open fun floor(query: QueryFloor): FloorResult {
        return repository.findByUuid(query.elevatorId.id)
                .map { FloorResult(it.floor) }
                .orElse(FloorResult(null))
    }
}
