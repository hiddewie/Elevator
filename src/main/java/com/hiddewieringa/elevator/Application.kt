package com.hiddewieringa.elevator

import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class Application {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    @Bean
    open fun commandLineRunner(ctx: ApplicationContext) = CommandLineRunner {
        logger.info("Listening on port 8080!")
    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
    SpringApplication.run(Application::class.java, *args)
}
