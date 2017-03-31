package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.*
import java.sql.Time
import java.time.LocalDate

interface AssignmentService{
    fun retrieveAssignments(service: Int, startDate: LocalDate): List<Assignment>
    fun shuttleDrop(service: Int): List<AssignmentShuttle>
    fun driverDrop(service: Int): List<AssignmentDriver>
    fun routeDrop(service: Int): List<AssignmentRoute>
    fun stopDrop(service: Int): List<AssignmentStopDrop>
    fun retrieveRouteStops(routeid: Int): List<AssignmentRouteStop>
    fun addAssignment(newAssignment: NewAssignment)
    fun archiveAssignment(assignmentid: Int)
}