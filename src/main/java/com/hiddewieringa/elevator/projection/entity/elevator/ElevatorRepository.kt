package com.hiddewieringa.elevator.projection.entity.elevator

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ElevatorRepository : PagingAndSortingRepository<Elevator, UUID>
