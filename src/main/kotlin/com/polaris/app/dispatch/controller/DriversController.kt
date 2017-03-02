package com.polaris.app.dispatch.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class DriversController {

    val DRIVERS_PAGE = "drivers"

    @RequestMapping("/drivers")
    fun drivers() : String {
        return DRIVERS_PAGE
    }
}