package com.polaris.app.dispatch.repository

//import com.polaris.app.dispatch.repository.entity.MapAssignmentEntity
import com.polaris.app.dispatch.repository.entity.MapShuttleEntity
import com.polaris.app.dispatch.repository.entity.MapAssignStopEntity
import com.polaris.app.dispatch.repository.entity.MapDriverEntity
import com.polaris.app.dispatch.service.bo.MapShuttle

interface MapRepository{
    fun findActiveShuttles(service: Int): List<MapShuttleEntity>
    fun findShuttleDriver(shuttle: MapShuttleEntity): List<MapDriverEntity>
    fun findActiveAssignmentInfo(shuttle: MapShuttle): List<MapAssignStopEntity>
    //fun findAssignStops(AssignID: Int): List<MapAssignStopEntity>
}

