package com.polaris.app.dispatch.controller

import com.polaris.app.dispatch.controller.exception.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException::class)
    fun exceptionHandler(e: AuthenticationException, attributes: RedirectAttributes) : String {
        attributes.addFlashAttribute("error", e.message)
        return "redirect:/loginForm"
    }
}