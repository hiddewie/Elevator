package com.hiddewieringa.elevator

import org.axonframework.eventhandling.EventHandler
import org.axonframework.spring.eventhandling.scheduling.java.SimpleEventSchedulerFactoryBean
import org.omg.CORBA.Object
import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@SpringBootApplication
open class Application {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    @Bean
    open fun commandLineRunner(ctx: ApplicationContext) = CommandLineRunner {
        logger.info("Listening on port 8080!")
    }

    @Bean
    open fun eventScheduler() = SimpleEventSchedulerFactoryBean()

}

@Service
open class Listener {
    val logger = LoggerFactory.getLogger(Listener::class.java)

    @EventHandler
    open fun on(o: Object) {
        logger.info("Event ${o}")
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
