package com.polaris.app.dispatch.service.impl

import com.polaris.app.dispatch.repository.MapRepository
import com.polaris.app.dispatch.service.MapService
import com.polaris.app.dispatch.service.bo.MapAssignment
import com.polaris.app.dispatch.service.bo.MapAssignmentStop
import com.polaris.app.dispatch.service.bo.MapShuttle

class MapServiceImpl(val MapRepository: MapRepository): MapService{
    override fun retrieveShuttle(): List<MapShuttle> {
        val mapShuttles = arrayListOf<MapShuttle>()
        val mapShuttleEntities = this.MapRepository.findActiveShuttles(0)

        mapShuttleEntities.forEach {
            val mapShuttle = MapShuttle(
                    ShuttleID = it.ShuttleID,
                    ShuttleIconColor = it.ShuttleIconColor,
                    ShuttleLat = it.ShuttleLat,
                    ShuttleLong = it.ShuttleLong,
                    ShuttleName = it.ShuttleName,
                    ShuttleStatus = it.ShuttleStatus
            )
            mapShuttles.add(mapShuttle)
        }
        return mapShuttles
    }

    override fun retrieveAssignments(): List<MapAssignment> {
        val mapAssignments = arrayListOf<MapAssignment>()
        val mapAssignmentEntities = this.MapRepository.findActiveAssignments()
        //val mapAssignmentStopEntities = this.MapRepository.findAssignStops()

        mapAssignmentEntities.forEach {
            val mapAssignmentStops = arrayListOf<MapAssignmentStop>()
            val mapAssignmentStopEntities = this.MapRepository.findAssignStops(it.AssignmentID)
            mapAssignmentStopEntities.forEach{
                val mapAssignmentStop = MapAssignmentStop(
                        stopName = it.stopName,
                        stopAddress = it.stopAddress,
                        stopLat = it.stopLat,
                        stopLong = it.stopLong,
                        stopArrive = it.stopArrive,
                        stopDepart = it.stopDepart,
                        stopArriveEst = it.stopArriveEst,
                        stopDepartEst = it.stopDepartEst
                )
                mapAssignmentStops.add(mapAssignmentStop)
            }
            val mapAssignment = MapAssignment(
                    AssignmentID = it.AssignmentID,
                    DriverID = it.DriverID,
                    DriverName = it.DriverName,
                    Stops = mapAssignmentStops
            )
            mapAssignments.add(mapAssignment)
        }
        return mapAssignments
    }
}