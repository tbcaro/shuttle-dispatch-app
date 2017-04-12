package com.polaris.app.dispatch.controller

import com.polaris.app.dispatch.controller.exception.AuthenticationException
import com.polaris.app.dispatch.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class StopsController(private val authService: AuthenticationService) {

    @RequestMapping("/stops")
    fun stops(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            model.addAttribute("title", "Stops")
            model.addAttribute("username", userContext.username)

            return "stops"
        } else {
            throw AuthenticationException()
        }
    }
}