package com.polaris.app.dispatch

import com.polaris.app.dispatch.repository.pg.HelloWorldPgRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import com.fasterxml.jackson.databind.ObjectMapper
import com.polaris.app.dispatch.repository.AssignmentRepository
import com.polaris.app.dispatch.repository.MapRepository
import com.polaris.app.dispatch.repository.StopRepository
import com.polaris.app.dispatch.repository.pg.MapPgRepository
import com.polaris.app.dispatch.repository.pg.UserPgRepository
import com.polaris.app.dispatch.service.*
import com.polaris.app.dispatch.service.impl.*
import org.springframework.jdbc.datasource.DataSourceTransactionManager


@SpringBootApplication
open class Application {
    @Autowired
    lateinit var helloWorldRepo: HelloWorldPgRepository

    @Autowired
    lateinit var mapRepository: MapPgRepository

    @Autowired
    lateinit var userRepo: UserPgRepository

    @Autowired
    lateinit var assignmentRepo: AssignmentRepository

    @Autowired
    lateinit var stopRepo: StopRepository

//    @Autowired
//    lateinit var transactionManager: DataSourceTransactionManager

//    @Bean
//    open fun transactionManager(): DataSourceTransactionManager {
//        return DataSourceTransactionManager()
//    }
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

    @Bean
    open fun assignmentService(): AssignmentService {
        return AssignmentServiceImpl(assignmentRepo)
    }

    @Bean
    open fun stopService(): StopService {
        return StopServiceImpl(stopRepo)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)

    val mapper = ObjectMapper()
    mapper.findAndRegisterModules()
}


