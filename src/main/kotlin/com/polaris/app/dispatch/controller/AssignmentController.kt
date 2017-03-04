package com.polaris.app.dispatch.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AssignmentController {

    @RequestMapping("/assignments")
    fun map(model: Model) : String {

        model.addAttribute("title", "Assignments")
        model.addAttribute("username", "tcaro")

        return "assignments"
    }
}