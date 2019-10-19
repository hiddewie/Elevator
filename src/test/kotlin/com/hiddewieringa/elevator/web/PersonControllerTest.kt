package com.hiddewieringa.elevator.web

import com.hiddewieringa.elevator.domain.person.AllQuery
import com.hiddewieringa.elevator.domain.person.PersonArrives
import com.hiddewieringa.elevator.domain.person.model.PersonId
import com.hiddewieringa.elevator.projection.entity.person.Person
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventBus
import org.axonframework.messaging.responsetypes.ResponseType
import org.axonframework.queryhandling.DefaultSubscriptionQueryResult
import org.axonframework.queryhandling.QueryGateway
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@RunWith(SpringRunner::class)
@WebFluxTest(controllers = [PersonController::class])
class PersonControllerTest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var commandGateway: CommandGateway
    @MockBean
    private lateinit var queryGateway: QueryGateway
    @MockBean
    private lateinit var eventBus: EventBus

    @Test
    fun allPersons() {
        val queryResult =
            DefaultSubscriptionQueryResult(Mono.just(emptyList<Person>()), Flux.empty<List<Person>>(), null)
        `when`(
            queryGateway.subscriptionQuery(any(AllQuery::class.java), any<ResponseType<*>>(), any<ResponseType<*>>())
        ).thenReturn(queryResult)

        val body = this.webClient.get().uri("/person")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType("text/event-stream;charset=UTF-8")
            .expectBody(String::class.java).returnResult()

        assertThat(body.responseBody!!, containsString("data:[]"))
    }

    @Test
    fun arrive() {
        val body = this.webClient.get().uri("/person/arrive/0/1")
            .exchange()
            .expectStatus().isOk
            .expectBody(UUID::class.java).returnResult()

        verify(commandGateway, times(1))
            .send<Any>(PersonArrives(PersonId(body.responseBody!!), 0, 1))
    }
}
