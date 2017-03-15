package com.polaris.app.dispatch.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class RoutesController {

    @RequestMapping("/routes")
    fun routes(model: Model) : String {

        model.addAttribute("title", "Routes")
        model.addAttribute("username", "tcaro")

        return "routes"
    }
}