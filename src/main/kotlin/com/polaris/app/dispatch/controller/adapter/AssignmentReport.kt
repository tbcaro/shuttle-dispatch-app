package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState


class AssignmentReport {
    var assignmentId: Int = 0
    var assignmentStops: List<AssignmentStopAdapter> = arrayListOf()
    var currentStop: Int = 0
    var assignmentStatus: AssignmentState = AssignmentState.NONE
}