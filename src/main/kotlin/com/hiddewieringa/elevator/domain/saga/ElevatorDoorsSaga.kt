package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.CloseDoors
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsAutoClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.eventhandling.scheduling.ScheduleToken
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.stereotype.Service
import java.time.Duration

@Saga
class ElevatorDoorsSaga {

    var scheduleToken: ScheduleToken? = null

    @StartSaga(forceNew = true)
    @SagaEventHandler(associationProperty = "elevatorId")
    fun opened(event: ElevatorDoorsOpened, eventScheduler: EventScheduler) {
        if (scheduleToken != null) {
            eventScheduler.cancelSchedule(scheduleToken)
            scheduleToken = null
        }

        scheduleToken = eventScheduler.schedule(Duration.ofSeconds(3), ElevatorDoorsAutoClosed(event.elevatorId))
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun closed(event: ElevatorDoorsClosed, eventScheduler: EventScheduler) {
        if (scheduleToken != null) {
            eventScheduler.cancelSchedule(scheduleToken)
            scheduleToken = null
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun closed(event: ElevatorDoorsAutoClosed) {
    }
}

@Service
class SagaDoorsAutoCloser {

    @EventHandler
    fun close(event: ElevatorDoorsAutoClosed, commandGateway: CommandGateway) {
        commandGateway.send(CloseDoors(event.elevatorId), LoggingCallback.INSTANCE)
    }
}
