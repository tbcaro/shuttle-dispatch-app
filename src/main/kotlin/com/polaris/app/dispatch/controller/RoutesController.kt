package com.polaris.app.dispatch.controller

import com.polaris.app.dispatch.controller.exception.AuthenticationException
import com.polaris.app.dispatch.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class RoutesController(private val authService: AuthenticationService) {

    @RequestMapping("/routes")
    fun routes(model: Model, http: HttpServletRequest) : String {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)
            model.addAttribute("title", "Routes")
            model.addAttribute("username", userContext.username)

            return "routes"
        } else {
            throw AuthenticationException()
        }
    }
}