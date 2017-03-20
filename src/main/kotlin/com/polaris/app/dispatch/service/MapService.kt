package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.MapShuttle
import com.polaris.app.dispatch.service.bo.MapAssignment

interface MapService{
    fun retrieveShuttle(): List<MapShuttle>
    fun retrieveAssignments(): List<MapAssignment>
}
