package com.hiddewieringa.elevator.web

import com.hiddewieringa.elevator.domain.person.PersonArrives
import com.hiddewieringa.elevator.domain.person.model.PersonId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/person")
class PersonController(@Autowired val commandGateway: CommandGateway) {

    @RequestMapping("/arrive/{floor}/{requestedFloor}")
    fun arrive(@PathVariable("floor") floor: Long, @PathVariable("requestedFloor") requestedFloor: Long): UUID {
        val id = UUID.randomUUID()
        commandGateway.send<Any>(PersonArrives(PersonId(id), floor, requestedFloor))
        return id
    }
}
