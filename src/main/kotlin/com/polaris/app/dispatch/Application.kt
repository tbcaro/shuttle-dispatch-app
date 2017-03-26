package com.polaris.app.dispatch

import com.polaris.app.dispatch.repository.pg.HelloWorldPgRepository
import com.polaris.app.dispatch.service.HelloWorldService
import com.polaris.app.dispatch.service.impl.HelloWorldServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import com.fasterxml.jackson.databind.ObjectMapper



@SpringBootApplication
open class Application {
    @Autowired
    lateinit var helloWorldRepo: HelloWorldPgRepository

    @Bean
    open fun helloWorldService(): HelloWorldService {
        return HelloWorldServiceImpl(helloWorldRepo)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)

    val mapper = ObjectMapper()
    mapper.findAndRegisterModules()
}


