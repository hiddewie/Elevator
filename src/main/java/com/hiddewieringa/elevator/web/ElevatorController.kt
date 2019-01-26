package com.hiddewieringa.elevator.web

import com.hiddewieringa.elevator.domain.elevator.CallElevator
import com.hiddewieringa.elevator.domain.elevator.CreateElevator
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.domain.elevator.query.ActiveQuery
import com.hiddewieringa.elevator.domain.elevator.query.FloorResult
import com.hiddewieringa.elevator.domain.elevator.query.QueryFloor
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.responsetypes.ResponseTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.LocalTime
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@RestController
@RequestMapping("/elevator")
class ElevatorController(@Autowired val commandGateway: CommandGateway,
                         @Autowired val queryGateway: QueryGateway
) {

    @RequestMapping("/create")
    fun create(): UUID {
        val id = UUID.randomUUID()
        commandGateway.send<Any>(CreateElevator(ElevatorId(id)))
        return id
    }

    @RequestMapping("/{id}/call/{floor}")
    fun index(@PathVariable id: UUID, @PathVariable floor: Long): String {
        commandGateway.send<Any>(CallElevator(ElevatorId(id), floor))
        return ""
    }

    @GetMapping
    fun elevators(): CompletableFuture<List<Elevator>> {
        return queryGateway.query(ActiveQuery(), ResponseTypes.multipleInstancesOf(Elevator::class.java))
    }

    @GetMapping("/events")
    fun elevatorEvents(): SseEmitter {
        val emitter = SseEmitter()
        val sseMvcExecutor = Executors.newSingleThreadExecutor()
        sseMvcExecutor.execute {
            try {
                var i = 0
                while (true) {
                    val event = SseEmitter.event()
                            .data("SSE MVC - " + LocalTime.now().toString())
                            .id(i.toString())
                            .name("sse event - mvc")
                    emitter.send(event)
                    Thread.sleep(1000)
                    i++
                }
            } catch (ex: Exception) {
                emitter.completeWithError(ex)
            }
        }
        return emitter
    }

    @RequestMapping("/{id}/floor")
    fun floor(@PathVariable id: UUID): CompletableFuture<FloorResult> {
        return queryGateway.query(QueryFloor(ElevatorId(id)), FloorResult::class.java)
    }
}
