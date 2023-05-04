package com.hiddewieringa.elevator.web

import com.hiddewieringa.elevator.domain.elevator.ActiveQuery
import com.hiddewieringa.elevator.domain.elevator.AssignElevatorTarget
import com.hiddewieringa.elevator.domain.elevator.CreateElevator
import com.hiddewieringa.elevator.domain.elevator.FloorResult
import com.hiddewieringa.elevator.domain.elevator.QueryFloor
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/elevator")
class ElevatorController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
) {

    @RequestMapping("/create")
    fun create(): Mono<UUID> {
        val id = UUID.randomUUID()
        commandGateway.send<Any>(CreateElevator(ElevatorId(id)))
        return Mono.just(id)
    }

    @RequestMapping("/{id}/call/{floor}")
    fun call(@PathVariable id: UUID, @PathVariable floor: Long): Mono<String> {
        commandGateway.send<Any>(AssignElevatorTarget(ElevatorId(id), floor))
        return Mono.just(id.toString())
    }

    @GetMapping(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun elevators(): Flux<List<Elevator>> {
        val queryResult = queryGateway.subscriptionQuery(
            ActiveQuery(),
            ResponseTypes.multipleInstancesOf(Elevator::class.java),
            ResponseTypes.multipleInstancesOf(Elevator::class.java),
        )

        return Flux.create<List<Elevator>> { emitter ->
            queryResult.initialResult().subscribe { emitter.next(it) }
            queryResult.updates().doOnComplete(emitter::complete).subscribe { emitter.next(it) }
        }
    }

    @RequestMapping("/{id}/floor")
    fun floor(@PathVariable id: UUID): CompletableFuture<FloorResult> =
        queryGateway.query(QueryFloor(ElevatorId(id)), FloorResult::class.java)
}
