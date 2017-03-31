package com.polaris.app.dispatch

import com.polaris.app.dispatch.repository.pg.HelloWorldPgRepository
import com.polaris.app.dispatch.service.HelloWorldService
import com.polaris.app.dispatch.service.impl.HelloWorldServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import com.fasterxml.jackson.databind.ObjectMapper
import com.polaris.app.dispatch.repository.MapRepository
import com.polaris.app.dispatch.repository.pg.MapPgRepository
import com.polaris.app.dispatch.service.MapService
import com.polaris.app.dispatch.service.impl.MapServiceImpl
import com.polaris.app.dispatch.repository.pg.UserPgRepository
import com.polaris.app.dispatch.service.AuthenticationService
import com.polaris.app.dispatch.service.impl.AuthenticationServiceImpl


@SpringBootApplication
open class Application {
    @Autowired
    lateinit var helloWorldRepo: HelloWorldPgRepository

    @Autowired
    lateinit var mapRepository: MapPgRepository

    @Autowired
    lateinit var userRepo: UserPgRepository

    @Bean
    open fun authService(): AuthenticationService {
        return AuthenticationServiceImpl(userRepo)
    }

    @Bean
    open fun helloWorldService(): HelloWorldService {
        return HelloWorldServiceImpl(helloWorldRepo)
    }

    @Bean
    open fun mapService(): MapService {
        return MapServiceImpl(mapRepository)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)

    val mapper = ObjectMapper()
    mapper.findAndRegisterModules()
}


