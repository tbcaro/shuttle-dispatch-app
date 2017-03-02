package com.polaris.app.dispatch.controller

import com.polaris.app.dispatch.controller.adapter.HelloWorldViewAdapter
import com.polaris.app.dispatch.service.impl.HelloWorldServiceImpl
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class HelloWorldController(private val helloWorldService: HelloWorldServiceImpl) {

    val HELLO_WORLD_PAGE = "helloworld"

    @RequestMapping("/helloworld")
    fun map() : String {
        var helloWorldViewAdapter = HelloWorldViewAdapter()
        var errors = ArrayList<String>()

        try {
            val helloWorlds = helloWorldService.retrieveAll()
            helloWorldViewAdapter.helloWorld = helloWorlds[0].helloWorld
        } catch (ex: Exception) {
            errors.add(ex.message ?: "")
        }

        return HELLO_WORLD_PAGE
    }
}