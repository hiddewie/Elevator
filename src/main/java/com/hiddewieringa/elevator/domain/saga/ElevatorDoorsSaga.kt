package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.eventhandling.scheduling.ScheduleToken
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import java.time.Duration

@Saga
class ElevatorDoorsSaga {

    private lateinit var scheduleToken: ScheduleToken

    @StartSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun opened(event: ElevatorDoorsOpened, eventScheduler: EventScheduler) {
        scheduleToken = eventScheduler.schedule(Duration.ofSeconds(10), ElevatorDoorsClosed(event.elevatorId))
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun closed(event: ElevatorDoorsClosed, eventScheduler: EventScheduler) {
        eventScheduler.cancelSchedule(scheduleToken)
    }
}
