package com.polaris.app.dispatch.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MapController {

    val MAP_PAGE = "map"

    @RequestMapping("/map")
    fun map() : String {
        return MAP_PAGE
    }
}