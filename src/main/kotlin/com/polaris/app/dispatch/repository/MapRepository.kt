package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.MapAssignmentEntity
import com.polaris.app.dispatch.repository.entity.MapShuttleEntity
import com.polaris.app.dispatch.repository.entity.MapAssignStopEntity

interface MapRepository{
    fun findActiveShuttles(service: Int): List<MapShuttleEntity>
    fun findActiveAssignments(): List<MapAssignmentEntity>
    fun findAssignStops(AssignID: Int): List<MapAssignStopEntity>
}

