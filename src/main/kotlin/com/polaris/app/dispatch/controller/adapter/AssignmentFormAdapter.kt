package com.polaris.app.dispatch.controller.adapter

import com.google.common.collect.Multimap
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentFieldTags
import com.polaris.app.dispatch.service.bo.AssignmentStop
import java.time.LocalTime


class AssignmentFormAdapter {
    var startTime: LocalTime?
    var routeName: String
    var driverID: Int
    var shuttleID: Int
    var stops: List<AssignmentStop>

    constructor() {
        this.startTime = null
        this.routeName = ""
        this.driverID = 0
        this.shuttleID = 0
        this.stops = arrayListOf()
    }

    fun mapErrors(errors: Multimap<AssignmentFieldTags, String>) {
        //errors[AssignmentFieldTags.START_TIME].forEach {  }
    }
}