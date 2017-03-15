package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.DisplayDateFormatter
import java.time.LocalDate


class AssignmentListAdapter {
    var selectedDate = LocalDate.now()
    var assignmentDetailAdapters = arrayListOf<AssignmentDetailsAdapter>()
    val displayDate: String by lazy {
        val formatter = DisplayDateFormatter()
        formatter.format(this.selectedDate)
    }
}