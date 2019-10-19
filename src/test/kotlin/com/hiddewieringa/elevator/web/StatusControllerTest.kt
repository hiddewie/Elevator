package com.hiddewieringa.elevator.web

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventBus
import org.axonframework.queryhandling.QueryGateway
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@RunWith(SpringRunner::class)
@WebFluxTest
class StatusControllerTest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var commandGateway: CommandGateway
    @MockBean
    private lateinit var queryGateway: QueryGateway
    @MockBean
    private lateinit var eventBus: EventBus

    @Test
    fun live() {
        this.webClient.get().uri("/status/live")
            .exchange()
            .expectStatus().isOk
//            .expectBody(String::class.java) as WebTestClient.BodySpec<String, *>).isEqualTo<String>("OK")
    }

    @Test
    fun ready() {
        this.webClient.get().uri("/status/ready")
            .exchange()
            .expectStatus().isOk
//            .expectBody(String::class.java).isEqualTo("")
    }
}
