package com.polaris.app.dispatch.controller.adapter

import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentFieldTags
import com.polaris.app.dispatch.controller.adapter.form.FormField
import com.polaris.app.dispatch.controller.adapter.form.FormAdapter
import com.polaris.app.dispatch.controller.adapter.form.FormInt
import com.polaris.app.dispatch.controller.adapter.form.FormTime
import com.polaris.app.dispatch.service.bo.AssignmentStop
import java.time.LocalTime


class AssignmentFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var assignmentId: FormInt
    var shuttleId: FormInt
    var driverId: FormInt
    var routeId: FormInt
//    var startTime: FormTime
    var assignmentStopForms: List<AssignmentStopFormAdapter>

    constructor() {
        this.hasErrors = false
        this.assignmentId = FormInt(0)
        this.shuttleId = FormInt(0)
        this.driverId = FormInt(0)
        this.routeId = FormInt(0)
//        this.startTime = FormTime(null)
        this.assignmentStopForms = arrayListOf<AssignmentStopFormAdapter>()
    }

    fun mapErrors(errors: Multimap<AssignmentFieldTags, String>) {
        //errors[AssignmentFieldTags.START_TIME].forEach {  }
    }
}