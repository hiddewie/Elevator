package com.hiddewieringa.elevator.domain.elevator.query

import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service

@Service
class ElevatorQueryHandler {
    //
    //    private final Repository<ElevatorAggregate> elevatorAggregateRepository;
    //
    //    @Autowired
    //    public ElevatorQueryHandler(Repository<ElevatorAggregate> elevatorAggregateRepository) {
    //        this.elevatorAggregateRepository = elevatorAggregateRepository;
    //    }
    //
    @QueryHandler
    fun queryFloor(query: QueryFloor): FloorResult {
        //        Aggregate<ElevatorAggregate> q= elevatorAggregateRepository.load(query.getElevatorId().toString());
        return FloorResult(42)
    }
}
