package com.hiddewieringa.elevator

import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.spring.eventhandling.scheduling.java.SimpleEventSchedulerFactoryBean
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@SpringBootApplication
class Application {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    @Bean
    fun commandLineRunner(ctx: ApplicationContext) = CommandLineRunner {
        logger.info("Listening on port 8080!")
    }

    @Bean
    fun eventScheduler(): SimpleEventSchedulerFactoryBean = SimpleEventSchedulerFactoryBean()

    @Bean
    fun storageEngine(): EventStorageEngine = InMemoryEventStorageEngine()

    @Bean
    fun cors(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return CorsFilter(source)
    }
}

@Service
class Listener {
    val logger: Logger = LoggerFactory.getLogger(Listener::class.java)

    @EventHandler
    fun on(o: Any) {
        logger.info("Event $o")
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
