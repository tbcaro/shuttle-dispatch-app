package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.Assignment
import java.sql.Time

interface AssignmentService{
    fun retrieveAssignments(windowStart: Time, windowEnd: Time): List<Assignment>
    fun addAssignment(newAssignment: Assignment)
}