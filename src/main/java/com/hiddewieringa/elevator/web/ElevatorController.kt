package com.hiddewieringa.elevator.web

import com.hiddewieringa.elevator.domain.elevator.CallElevator
import com.hiddewieringa.elevator.domain.elevator.CreateElevator
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/elevator")
class ElevatorController(@Autowired val commandGateway: CommandGateway
//                          @Autowired val queryGateway: QueryGateway,
//                          @Autowired val queryBus: QueryBus
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

//    @RequestMapping("/{id}/floor")
//    @Throws(ExecutionException::class, InterruptedException::class)
//    fun floor(@PathVariable id: UUID): CompletableFuture<FloorResult> {
//        return queryGateway.query(QueryFloor(ElevatorId(id)), FloorResult::class.java)
//        //"test",
//        //        return queryBus.query(new GenericQueryMessage<>(new QueryFloor(new ElevatorId(id)), "test", ResponseTypes.instanceOf(FloorResult.class))).get().getPayload();
//    }
}
