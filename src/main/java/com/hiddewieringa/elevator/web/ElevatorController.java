package com.hiddewieringa.elevator.web;

import com.hiddewieringa.elevator.domain.elevator.command.CallElevator;
import com.hiddewieringa.elevator.domain.elevator.command.CreateElevator;
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/elevator")
public class ElevatorController {

    private final CommandBus commandBus;

    @Autowired
    public ElevatorController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @RequestMapping("/create")
    public UUID create() {
        final UUID id = UUID.randomUUID();
        final CreateElevator command = new CreateElevator(new ElevatorId(id));
        commandBus.dispatch(new GenericCommandMessage<>(command));

        return id;
    }

    @RequestMapping("/{id}/call/{floor}")
    public String index(@PathVariable UUID id, @PathVariable long floor) {
        final CallElevator command = new CallElevator(new ElevatorId(id), floor);
        commandBus.dispatch(new GenericCommandMessage<>(command));

        return "";
    }

    @RequestMapping("/{id}/floor")
    public long floor(@PathVariable UUID id) {
        return 0L;
    }
}
