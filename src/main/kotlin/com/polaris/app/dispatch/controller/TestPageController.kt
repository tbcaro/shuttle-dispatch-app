package com.polaris.app.dispatch.controller

import com.polaris.app.dispatch.DisplayDateFormatter
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Controller
@RequestMapping("/test")
class TestPageController {

    @RequestMapping("/assignments")
    fun assignments(
            @RequestParam("date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) date: LocalDate?,
            model: Model
    ) : String {
        // TBC : Default selected date to today
        var selectedDate: LocalDate = LocalDate.now()

        if (date != null) {
            selectedDate = date
        }
        val formatter = DisplayDateFormatter()

        model.addAttribute("title", "Assignments")
        model.addAttribute("username", "tcaro")
        model.addAttribute("selectedDate", selectedDate)
        model.addAttribute("displayDate", formatter.format(selectedDate))

        return "assignments"
    }

    @RequestMapping("/stops")
    fun stops(model: Model) : String {

        model.addAttribute("title", "Stops")
        model.addAttribute("username", "tcaro")

        return "stops"
    }

    @RequestMapping("/routes")
    fun routes(model: Model) : String {

        model.addAttribute("title", "Routes")
        model.addAttribute("username", "tcaro")

        return "routes"
    }
}