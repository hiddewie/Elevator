package com.hiddewieringa.elevator.web

import com.hiddewieringa.elevator.domain.elevator.ActiveQuery
import com.hiddewieringa.elevator.domain.person.AllQuery
import com.hiddewieringa.elevator.domain.person.PersonArrives
import com.hiddewieringa.elevator.domain.person.model.PersonId
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import com.hiddewieringa.elevator.projection.entity.person.Person
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.*

@RestController
@RequestMapping("/person")
class PersonController @Autowired constructor(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {

    @RequestMapping("/arrive/{floor}/{requestedFloor}")
    fun arrive(@PathVariable("floor") floor: Long, @PathVariable("requestedFloor") requestedFloor: Long): UUID {
        val id = UUID.randomUUID()
        commandGateway.send<Any>(PersonArrives(PersonId(id), floor, requestedFloor))
        return id
    }

    @GetMapping(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun persons(): Flux<List<Person>> {
        val queryResult = queryGateway.subscriptionQuery(
            AllQuery(),
            ResponseTypes.multipleInstancesOf(Person::class.java),
            ResponseTypes.multipleInstancesOf(Person::class.java)
        )

        return Flux.create<List<Person>> { emitter ->
            queryResult.initialResult().subscribe { emitter.next(it) }
            queryResult.updates().doOnComplete(emitter::complete).subscribe { emitter.next(it) }
        }
    }
}
