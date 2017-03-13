package com.polaris.app.dispatch.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MapController {

    @RequestMapping("/map")
    fun map(model: Model) : String {

        model.addAttribute("title", "Map")
        model.addAttribute("username", "tcaro")
        model.addAttribute("serviceCode", "test-service-code")

        return "map"
    }
}