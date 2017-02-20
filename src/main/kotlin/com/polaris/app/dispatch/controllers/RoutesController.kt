package com.polaris.app.dispatch.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class RoutesController {

    val ROUTES_PAGE = "routes"

    @RequestMapping("/routes")
    fun routes() : String {
        return ROUTES_PAGE
    }
}