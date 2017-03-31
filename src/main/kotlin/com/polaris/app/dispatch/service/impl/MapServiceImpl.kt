package com.polaris.app.dispatch.service.impl

import com.polaris.app.dispatch.repository.MapRepository
import com.polaris.app.dispatch.service.MapService
import com.polaris.app.dispatch.service.bo.MapAssignment
import com.polaris.app.dispatch.service.bo.MapAssignmentStop
import com.polaris.app.dispatch.service.bo.MapShuttle

class MapServiceImpl(val MapRepository: MapRepository): MapService{
    override fun retrieveShuttle(service: Int): List<MapShuttle> {
        val mapShuttles = arrayListOf<MapShuttle>()
        val mapShuttleEntities = this.MapRepository.findActiveShuttles(service)

        mapShuttleEntities.forEach {
            val mapShuttleDriverEntities = this.MapRepository.findShuttleDriver(it)

            var mapShuttle = MapShuttle(
                    shuttleID = it.shuttleID,
                    shuttleName = it.shuttleName,
                    shuttleIconColor = it.shuttleIconColor,
                    shuttleAssignmentID = it.shuttleAssignmentID,
                    shuttleLat = it.shuttleLat,
                    shuttleLong = it.shuttleLong,
                    shuttleStatus = it.shuttleStatus,
                    shuttleDriverID = it.shuttleDriverID,
                    heading = it.heading,
                    currentStopIndex = it.currentStopIndex,
                    shuttleDriverFName = "",
                    shuttleDriverLName = ""
            )
            if (mapShuttleDriverEntities.isNotEmpty()) {
                val driver = mapShuttleDriverEntities[0]
                mapShuttle = mapShuttle.copy(shuttleDriverFName = driver.driverFName, shuttleDriverLName = driver.driverLName)
            }
            mapShuttles.add(mapShuttle)
        }
        return mapShuttles
    }

    override fun retrieveAssignmentData(mapShuttle: MapShuttle): MapAssignment? {
        val mapAssignmentStops = arrayListOf<MapAssignmentStop>()

        if (mapShuttle.shuttleAssignmentID != null) {
            val mapAssignmentEntities = this.MapRepository.findActiveAssignmentInfo(mapShuttle)
            mapAssignmentEntities.forEach{
                val mapAssignmentStop = MapAssignmentStop(
                        assignmentStopId = it.assignmentStopId,
                        stopId = it.stopId,
                        index = it.index,
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
            return MapAssignment(
                    assignmentID = mapShuttle.shuttleAssignmentID,
                    driverID = mapShuttle.shuttleDriverID,
                    driverFName = mapShuttle.shuttleDriverFName,
                    driverLName = mapShuttle.shuttleDriverLName,
                    shuttleID = mapShuttle.shuttleID,
                    shuttleName = mapShuttle.shuttleName,
                    stops = mapAssignmentStops
            )
        } else {
            return null
        }

        //val mapAssignmentStopEntities = this.MapRepository.findAssignStops()

        /*mapAssignmentEntities.forEach {
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
        }*/
    }
}