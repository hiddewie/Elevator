package com.hiddewieringa.elevator.projection.entity.elevator

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ElevatorRepository : CrudRepository<Elevator, Long> {

    fun findByUuid(uuid: UUID): Optional<Elevator>
}
