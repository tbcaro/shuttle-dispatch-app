package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.MapShuttle
import com.polaris.app.dispatch.service.bo.MapAssignment

interface MapService{
    fun retrieveShuttle(service: Int): List<MapShuttle>
    fun retrieveAssignmentData(mapShuttle: MapShuttle): MapAssignment?
}
