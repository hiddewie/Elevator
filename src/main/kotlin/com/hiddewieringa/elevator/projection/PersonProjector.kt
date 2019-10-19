package com.hiddewieringa.elevator.projection

import com.hiddewieringa.elevator.domain.person.AllQuery
import com.hiddewieringa.elevator.domain.person.PersonArrived
import com.hiddewieringa.elevator.domain.person.PersonEnteredElevator
import com.hiddewieringa.elevator.domain.person.PersonLeftElevator
import com.hiddewieringa.elevator.projection.entity.person.Person
import com.hiddewieringa.elevator.projection.entity.person.PersonRepository
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.queryhandling.QueryHandler
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonProjector @Autowired constructor(
    private val queryUpdateEmitter: QueryUpdateEmitter,
    private val personRepository: PersonRepository
) {

    @EventSourcingHandler
    fun create(event: PersonArrived) {
        personRepository.save(
            Person(
                uuid = event.personId.id,
                inElevator = false,
                initialFloor = event.floor
            )
        )
        updateQueryResults()
    }

    @EventSourcingHandler
    fun enter(event: PersonEnteredElevator) {
        personRepository.findByUuid(event.personId.id).ifPresent {
            it.inElevator = true
            personRepository.save(it)
        }
        updateQueryResults()
    }

    @EventSourcingHandler
    fun enter(event: PersonLeftElevator) {
        personRepository.findByUuid(event.personId.id).ifPresent {
            personRepository.delete(it)
        }
        updateQueryResults()
    }

    @QueryHandler
    fun active(query: AllQuery) =
        personRepository.findAll()

    private fun updateQueryResults() {
        queryUpdateEmitter.emit(
            AllQuery::class.java,
            { true },
            personRepository.findAll()
        )
    }
}
