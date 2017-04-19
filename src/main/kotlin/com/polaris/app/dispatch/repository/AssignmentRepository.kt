package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.*
import com.polaris.app.dispatch.service.bo.AssignmentUpdate
import com.polaris.app.dispatch.service.bo.NewAssignment
import com.polaris.app.dispatch.service.bo.NewAssignmentStop
import java.time.LocalDate

interface AssignmentRepository {
    fun findAssignments(service: Int, date: LocalDate): List<AssignmentEntity>//windowStart and windowEnd will typically be midnight at the start of a day and 11:59PM at the end of the same day
    fun findAssignmentStops(assignID: Int): List<AssignmentStopEntity>
    fun findDropShuttles(service: Int): List<AssignmentShuttleEntity>
    fun findDropDrivers(service: Int): List<AssignmentDriverEntity>
    fun findDropRoutes(service: Int): List<AssignmentRouteEntity>
    fun findDropStops(service: Int): List<AssignmentStopDropEntity>
    fun findDropRouteStops(routeID: Int): List<AssignmentRouteStopEntity>
    fun addAssignment(a: NewAssignment): Int//Returns the newly created assignmentID
    fun addAssignmentStops(assignmentID: Int?, assignmentStop: List<NewAssignmentStop>)
    fun updateAssignment(ua: AssignmentUpdate)
    fun checkAssignment(assignmentID: Int?): AssignmentEntity
    fun checkIndex(assignmentID: Int?): Int
    fun removeAssignmentStops(assignmentID: Int?, index: Int?)
    fun archiveAssignment(assignmentID: Int)
    fun findAssignmentByAssignmentID(assignmentID: Int?): AssignmentEntity
    fun findStopIndex(assignmentStopID: Int): Int
    fun findStop(assignmentStopID: Int): AssignmentStopEntity
}