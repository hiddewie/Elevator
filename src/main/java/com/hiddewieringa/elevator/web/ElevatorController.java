package com.hiddewieringa.elevator.web;

import com.hiddewieringa.elevator.domain.elevator.command.CallElevator;
import com.hiddewieringa.elevator.domain.elevator.command.CreateElevator;
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId;
import com.hiddewieringa.elevator.domain.elevator.query.FloorResult;
import com.hiddewieringa.elevator.domain.elevator.query.QueryFloor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.GenericQueryMessage;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/elevator")
public class ElevatorController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final QueryBus queryBus;

    @Autowired
    public ElevatorController(CommandGateway commandGateway, QueryGateway queryGateway, QueryBus queryBus) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.queryBus = queryBus;
    }

    @RequestMapping("/create")
    public UUID create() {
        final UUID id = UUID.randomUUID();
        commandGateway.send(new CreateElevator(new ElevatorId(id)));
        return id;
    }

    @RequestMapping("/{id}/call/{floor}")
    public String index(@PathVariable UUID id, @PathVariable long floor) {
        commandGateway.send(new CallElevator(new ElevatorId(id), floor));
        return "";
    }

    @RequestMapping("/{id}/floor")
    public CompletableFuture<FloorResult> floor(@PathVariable UUID id) throws ExecutionException, InterruptedException {
        return queryGateway.query(new QueryFloor(new ElevatorId(id)), FloorResult.class);
        //"test",
//        return queryBus.query(new GenericQueryMessage<>(new QueryFloor(new ElevatorId(id)), "test", ResponseTypes.instanceOf(FloorResult.class))).get().getPayload();
    }
}
