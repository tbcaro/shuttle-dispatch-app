package com.polaris.app.dispatch.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AssignmentController {

    val ASSIGNMENTS_PAGE = "assignments"

    @RequestMapping("/assignments")
    fun assignments() : String {
        return ASSIGNMENTS_PAGE
    }
}