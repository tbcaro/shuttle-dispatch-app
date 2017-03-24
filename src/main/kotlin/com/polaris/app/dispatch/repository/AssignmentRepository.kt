package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.AssignmentEntity
import com.polaris.app.dispatch.repository.entity.AssignmentStopEntity
import java.sql.Time

interface AssignmentRepository {
    fun findAssignments(service: Int, windowStart: Time, windowEnd: Time): List<AssignmentEntity>//windowStart and windowEnd will typically be midnight at the start of a day and 11:59PM at the end of the same day
    fun findAssignmentStops(assignID: Int): List<AssignmentStopEntity>
    fun addAssignment(service: Int, Assignment: AssignmentEntity)
    //add function addAssignmentStops - Should always be performed with addAssignments
}