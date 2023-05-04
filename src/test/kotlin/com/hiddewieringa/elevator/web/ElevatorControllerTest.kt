package com.hiddewieringa.elevator.web

import com.hiddewieringa.elevator.domain.elevator.ActiveQuery
import com.hiddewieringa.elevator.domain.elevator.AssignElevatorTarget
import com.hiddewieringa.elevator.domain.elevator.CreateElevator
import com.hiddewieringa.elevator.domain.elevator.FloorResult
import com.hiddewieringa.elevator.domain.elevator.QueryFloor
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventBus
import org.axonframework.messaging.responsetypes.ResponseType
import org.axonframework.queryhandling.DefaultSubscriptionQueryResult
import org.axonframework.queryhandling.QueryGateway
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID
import java.util.concurrent.CompletableFuture

@RunWith(SpringRunner::class)
@WebFluxTest(controllers = [ElevatorController::class])
class ElevatorControllerTest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var commandGateway: CommandGateway

    @MockBean
    private lateinit var queryGateway: QueryGateway

    @MockBean
    private lateinit var eventBus: EventBus

    @Test
    fun create() {
        this.webClient.get().uri("/elevator/create")
            .exchange()
            .expectStatus().isOk

        verify(commandGateway).send<Any>(any(CreateElevator::class.java))
    }

    @Test
    fun call() {
        val id = UUID.randomUUID()
        this.webClient.get().uri("/elevator/$id/call/0")
            .exchange()
            .expectStatus().isOk

        verify(commandGateway).send<Any>(any(AssignElevatorTarget::class.java))
    }

    @Test
    fun elevators() {
        val queryResult =
            DefaultSubscriptionQueryResult(Mono.just(emptyList<Elevator>()), Flux.empty<List<Elevator>>(), null)

        `when`(
            queryGateway.subscriptionQuery(any(ActiveQuery::class.java), any<ResponseType<*>>(), any<ResponseType<*>>()),
        ).thenReturn(queryResult)

        val body = this.webClient.get().uri("/elevator")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType("text/event-stream;charset=UTF-8")
            .expectBody(String::class.java).returnResult()

        MatcherAssert.assertThat(body.responseBody!!, CoreMatchers.containsString("data:[]"))
    }

    @Test
    fun floor() {
        `when`(queryGateway.query(any<QueryFloor>(), any<Class<*>>()))
            .thenReturn(CompletableFuture.completedFuture(FloorResult(1)))

        val id = UUID.randomUUID()
        val body = this.webClient.get().uri("/elevator/$id/floor")
            .exchange()
            .expectStatus().isOk
            .expectBody(FloorResult::class.java).returnResult()

        Assert.assertEquals(body.responseBody!!, FloorResult(1))

        verify(queryGateway).query(any(QueryFloor::class.java), any(Class::class.java))
    }
}
