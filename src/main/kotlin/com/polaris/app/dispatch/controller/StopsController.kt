package com.polaris.app.dispatch.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class StopsController {

    @RequestMapping("/assignmentStops")
    fun stops(model: Model) : String {

        model.addAttribute("title", "Stops")
        model.addAttribute("username", "tcaro")

        return "assignmentStops"
    }
}