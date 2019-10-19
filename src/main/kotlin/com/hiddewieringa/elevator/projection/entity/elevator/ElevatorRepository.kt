package com.hiddewieringa.elevator.projection.entity.elevator

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface ElevatorRepository : PagingAndSortingRepository<Elevator, Long> {

    fun findByUuid(uuid: UUID): Optional<Elevator>
}
