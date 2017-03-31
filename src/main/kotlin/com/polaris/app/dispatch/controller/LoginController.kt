package com.polaris.app.dispatch.controller

import com.polaris.app.dispatch.controller.adapter.LoginAdapter
import com.polaris.app.dispatch.controller.exception.AuthenticationException
import com.polaris.app.dispatch.service.AuthenticationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest

@Controller
class LoginController(private val authService: AuthenticationService) {

    @RequestMapping("/")
    fun root(model: Model) : String {
        return "redirect:/loginForm"
    }

    @RequestMapping("/loginForm")
    fun loginForm(model: Model, attributes: RedirectAttributes) : String {
        attributes.asMap().forEach { model.addAttribute(it.key, it.value) }
        return "login"
    }

    @RequestMapping("/login")
    fun login(attributes: RedirectAttributes, http: HttpServletRequest, loginAdapter: LoginAdapter) : String {

        loginAdapter.let {
            if (it.username != null && it.password != null && it.servicecode != null) {
                authService.authenticate(http, it.username!!, it.password!!, it.servicecode!!)
            }
        }

        if (authService.isAuthenticated(http)) {
            return "redirect:/map"
        } else {
            throw AuthenticationException("login failed: service code, username, password combination invalid")
        }
    }

    @RequestMapping("/logout")
    fun logout(attributes: RedirectAttributes, http: HttpServletRequest) : String {
        authService.invalidateSession(http)
        attributes.addFlashAttribute("success", "logout successful")
        return "redirect:/loginForm"
    }
}