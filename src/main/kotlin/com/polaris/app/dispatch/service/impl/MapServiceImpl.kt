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
            val mapShuttle = MapShuttle(
                    shuttleID = it.shuttleID,
                    shuttleName = it.shuttleName,
                    shuttleIconColor = it.shuttleIconColor,
                    shuttleAssignmentID = it.shuttleAssignmentID,
                    shuttleLat = it.shuttleLat,
                    shuttleLong = it.shuttleLong,
                    shuttleStatus = it.shuttleStatus,
                    shuttleDriverID = it.shuttleDriverID,
                    shuttleDriverFName = mapShuttleDriverEntities.driverFName,
                    shuttleDriverLName = mapShuttleDriverEntities.driverLName
            )
            mapShuttles.add(mapShuttle)
        }
        return mapShuttles
    }

    override fun retrieveAssignmentData(mapShuttles: List<MapShuttle>): List<MapAssignment> {
        val mapAssignments = arrayListOf<MapAssignment>()
        val mapAssignmentStops = arrayListOf<MapAssignmentStop>()

        mapShuttles.forEach{
            val mapAssignmentEntities = this.MapRepository.findActiveAssignmentInfo(it)
            mapAssignmentEntities.forEach{
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
                    assignmentID = it.shuttleAssignmentID,
                    driverID = it.shuttleDriverID,
                    driverFName = it.shuttleDriverFName,
                    driverLName = it.shuttleDriverLName,
                    shuttleID = it.shuttleID,
                    shuttleName = it.shuttleName,
                    stops = mapAssignmentStops
            )
            mapAssignments.add(mapAssignment)
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
        return mapAssignments
    }
}