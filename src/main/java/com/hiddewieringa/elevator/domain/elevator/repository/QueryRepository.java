package com.hiddewieringa.elevator.domain.elevator.repository;

import com.hiddewieringa.elevator.domain.elevator.query.FloorResult;
import com.hiddewieringa.elevator.domain.elevator.query.QueryFloor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Service
public class QueryRepository {

//    @Autowired
//    private Repository<ElevatorAggregate> elevatorAggregateRepository;

//    @Autowired
//    public QueryRepository(Repository<ElevatorAggregate> elevatorAggregateRepository) {
//        this.elevatorAggregateRepository = elevatorAggregateRepository;
//    }

    @QueryHandler
    public FloorResult queryFloor(QueryFloor queryFloor) {
//        Aggregate<ElevatorAggregate> q = elevatorAggregateRepository.load(queryFloor.getElevatorId().toString());
//        return new FloorResult(q.invoke(ElevatorAggregate::getFloor));
        return new FloorResult(42);
    }
}
