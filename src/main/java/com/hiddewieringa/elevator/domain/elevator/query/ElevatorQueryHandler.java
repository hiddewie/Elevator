package com.hiddewieringa.elevator.domain.elevator.query;

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorAggregate;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.spring.config.AnnotationDriven;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElevatorQueryHandler {
//
//    private final Repository<ElevatorAggregate> elevatorAggregateRepository;
//
//    @Autowired
//    public ElevatorQueryHandler(Repository<ElevatorAggregate> elevatorAggregateRepository) {
//        this.elevatorAggregateRepository = elevatorAggregateRepository;
//    }
//
//    @QueryHandler
//    public FloorResult queryFloor(QueryFloor query) {
//        Aggregate<ElevatorAggregate> q= elevatorAggregateRepository.load(query.getElevatorId().toString());
//        return new FloorResult(q.invoke(ElevatorAggregate::getFloor));
//    }
}
