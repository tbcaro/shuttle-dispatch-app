package com.polaris.app.dispatch.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ShuttlesController {

    val SHUTTLES_PAGE = "shuttles"

    @RequestMapping("/shuttles")
    fun shuttles() : String {
        return SHUTTLES_PAGE
    }
}