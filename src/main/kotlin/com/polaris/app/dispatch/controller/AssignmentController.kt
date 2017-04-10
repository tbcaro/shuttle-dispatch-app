package com.polaris.app.dispatch.controller

import com.polaris.app.dispatch.DisplayDateFormatter
import com.polaris.app.dispatch.controller.exception.AuthenticationException
import com.polaris.app.dispatch.service.AuthenticationService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

@Controller
class AssignmentController(private val authService: AuthenticationService) {

    @RequestMapping("/assignments")
    fun assignments(
            model: Model,
            http: HttpServletRequest,
            @RequestParam("date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) date: LocalDate?
    ) : String {
        if (authService.isAuthenticated(http)) {
            val userContext = authService.getUserContext(http)

            // TBC : Default selected date to today
            var selectedDate: LocalDate = LocalDate.now()

            if (date != null) {
                selectedDate = date
            }
            val formatter = DisplayDateFormatter()

            model.addAttribute("selectedDate", selectedDate)
            model.addAttribute("displayDate", formatter.format(selectedDate))
            model.addAttribute("title", "Assignments")
            model.addAttribute("username", userContext.username)

            return "assignments"
        } else {
            throw AuthenticationException()
        }
    }
}