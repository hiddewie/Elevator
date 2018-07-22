package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened
import org.axonframework.eventhandling.saga.EndSaga
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.axonframework.eventhandling.saga.StartSaga
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.eventhandling.scheduling.ScheduleToken
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration

@Saga
class ElevatorDoorsSaga {

    @Autowired
    @Transient
    private lateinit var eventScheduler: EventScheduler

    private lateinit var scheduleToken: ScheduleToken

    @StartSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun opened(event: ElevatorDoorsOpened) {
        scheduleToken = eventScheduler.schedule(Duration.ofSeconds(10), ElevatorDoorsClosed(event.elevatorId))
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun closed(event: ElevatorDoorsClosed) {
        eventScheduler.cancelSchedule(scheduleToken)
    }
}
