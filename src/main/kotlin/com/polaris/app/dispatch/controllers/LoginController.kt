package com.polaris.app.dispatch.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LoginController {

    val LOGIN_PAGE = "login"

    @RequestMapping("/login")
    fun login() : String {

        // TBC : TODO : Determine method to test if user is logged in (call API?)
        // TBC : TODO : If logged in, go to map, else show login form

        return LOGIN_PAGE
    }
}