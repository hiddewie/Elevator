package com.hiddewieringa.elevator.query

import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import com.hiddewieringa.elevator.projection.entity.elevator.ElevatorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class ElevatorQueryService {

    @Autowired
    private lateinit var repository: ElevatorRepository

    open fun active(): List<Elevator> {
        return repository.findAll().toList()
    }
}
