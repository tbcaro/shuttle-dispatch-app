package com.polaris.app.dispatch.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class StopsController {

    val STOPS_PAGE = "stops"

    @RequestMapping("/stops")
    fun stops() : String {
        return STOPS_PAGE
    }
}