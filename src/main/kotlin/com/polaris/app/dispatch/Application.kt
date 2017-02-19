package com.polaris.app.dispatch

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Application { }

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}


