package com.polaris.app.dispatch.controller.adapter

import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentFieldTags
import com.polaris.app.dispatch.service.bo.AssignmentStop
import java.time.LocalTime


class AssignmentFormAdapter : FormAdapter {
    override var hasErrors: Boolean

    var assignmentId: Field<Int?>
    var shuttleId: Field<Int>
    var driverId: Field<Int>
    var routeId: Field<Int>
    var startTime: Field<LocalTime>?
    var assignmentStops: Field<List<AssignmentStopFormAdapter>>

    constructor() {
        this.hasErrors = false;
        this.assignmentId = Field<Int?>(0)
        this.shuttleId = Field<Int>(0)
        this.driverId = Field<Int>(0)
        this.routeId = Field<Int>(0)
        this.startTime = null
        this.assignmentStops = Field<List<AssignmentStopFormAdapter>>(arrayListOf<AssignmentStopFormAdapter>())
    }

    fun mapErrors(errors: Multimap<AssignmentFieldTags, String>) {
        //errors[AssignmentFieldTags.START_TIME].forEach {  }
    }
}