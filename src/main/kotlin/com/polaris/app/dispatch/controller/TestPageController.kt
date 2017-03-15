package com.polaris.app.dispatch.controller

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
            @RequestParam("date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate?,
            model: Model
    ) : String {
        // TBC : Default selected date to today
        var selectedDate: LocalDate = LocalDate.now()

        // TBC : If date is set, override selected date to the date passed in.
        date?.let { selectedDate = date }

        // TBC : Build string 'DayOfWeek, Month dayNum, Year' -> 'Saturday, March 18, 2017
        var displayDateBuilder = StringBuilder()
        displayDateBuilder.append(selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US))
        displayDateBuilder.append(", ")
        displayDateBuilder.append(selectedDate.month.getDisplayName(TextStyle.FULL, Locale.US))
        displayDateBuilder.append(" ")
        displayDateBuilder.append(selectedDate.dayOfMonth)
        when (selectedDate.dayOfMonth.toString().get(selectedDate.dayOfMonth.toString().lastIndex)) {
            "1".toCharArray().first() -> displayDateBuilder.append("st")
            "2".toCharArray().first() -> displayDateBuilder.append("nd")
            "3".toCharArray().first() -> displayDateBuilder.append("rd")
            else -> {
                displayDateBuilder.append("th")
            }
        }
        displayDateBuilder.append(", ")
        displayDateBuilder.append(selectedDate.year)

        model.addAttribute("title", "Assignments")
        model.addAttribute("username", "tcaro")
        model.addAttribute("selectedDate", selectedDate)
        model.addAttribute("displayDate", displayDateBuilder.toString())

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