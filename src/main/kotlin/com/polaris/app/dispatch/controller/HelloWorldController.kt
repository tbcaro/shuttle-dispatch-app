package com.polaris.app.dispatch.controller

import com.polaris.app.dispatch.controller.adapter.HelloWorldViewAdapter
import com.polaris.app.dispatch.service.HelloWorldService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
class HelloWorldController(private val helloWorldService: HelloWorldService) {

    @RequestMapping("/helloworld")
    fun helloworld(model: Model) : String {
        var helloWorldViewAdapter = HelloWorldViewAdapter()
        var errors = ArrayList<String>()

        try {
            val helloWorlds = helloWorldService.retrieveAll()

            helloWorldViewAdapter.helloWorld = helloWorlds[0].helloWorld

            model.addAttribute("helloWorldViewAdapter", helloWorldViewAdapter)
        } catch (ex: Exception) {
            errors.add(ex.message ?: "")
        }

        return "helloworld"
    }
}