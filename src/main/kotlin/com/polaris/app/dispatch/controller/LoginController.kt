package com.polaris.app.dispatch.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LoginController {

    @RequestMapping("/")
    fun root(model: Model) : String {
        return "redirect:/loginForm"
    }

    @RequestMapping("/loginForm")
    fun loginForm(model: Model) : String {
        return "login"
    }

    @RequestMapping("/login")
    fun login(model: Model) : String {
        val loggedIn = true

        // TBC : Just for showing off testing idea
        if (loggedIn) {
            return "map"
        } else {
            return "login"
        }
    }
}