package com.hiddewieringa.elevator.projection.entity.person

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PersonRepository : PagingAndSortingRepository<Person, Long> {

    fun findByUuid(uuid: UUID): Optional<Person>

}
