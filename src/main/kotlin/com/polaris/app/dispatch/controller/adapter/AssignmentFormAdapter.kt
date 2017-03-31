package com.polaris.app.dispatch.controller.adapter

import com.fasterxml.jackson.annotation.JsonFormat
import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentFieldTags
import com.polaris.app.dispatch.controller.adapter.form.*
import com.polaris.app.dispatch.service.bo.AssignmentStop
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalTime


class AssignmentFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var assignmentId: FormInt
    var shuttleId: FormInt
    var driverId: FormInt
    var routeId: FormInt
    var startTime: FormDateTime
    var assignmentStopForms: List<AssignmentStopFormAdapter>

    constructor() {
        this.hasErrors = false
        this.assignmentId = FormInt(0)
        this.shuttleId = FormInt(0)
        this.driverId = FormInt(0)
        this.routeId = FormInt(0)
        this.startTime = FormDateTime(null)
        this.assignmentStopForms = arrayListOf<AssignmentStopFormAdapter>()
    }

    fun mapErrors(errors: Multimap<AssignmentFieldTags, String>) {
        //errors[AssignmentFieldTags.START_TIME].forEach {  }
    }
}