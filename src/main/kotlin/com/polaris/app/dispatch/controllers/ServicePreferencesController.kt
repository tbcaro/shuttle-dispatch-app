package com.polaris.app.dispatch.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ServicePreferencesController {

    val PREFERENCES_PAGE = "preferences"

    @RequestMapping("/preferences")
    fun preferences() : String {
        return PREFERENCES_PAGE
    }
}