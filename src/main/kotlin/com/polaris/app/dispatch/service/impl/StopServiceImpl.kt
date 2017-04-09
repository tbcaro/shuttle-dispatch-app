package com.polaris.app.dispatch.service.impl

import com.polaris.app.dispatch.repository.StopRepository
import com.polaris.app.dispatch.repository.entity.StopEntity
import com.polaris.app.dispatch.repository.entity.UpdateStopEntity
import com.polaris.app.dispatch.service.StopService
import com.polaris.app.dispatch.service.bo.Stop
import com.polaris.app.dispatch.service.bo.UpdateStop

class StopServiceImpl(val StopRepository:StopRepository): StopService{
    override fun retrieveStops(service: Int): List<Stop> {
        val stops = arrayListOf<Stop>()
        val stopEntities = this.StopRepository.findStops(service)

        stopEntities.forEach {
            val stop = Stop(
                    stopName = it.stopName,
                    stopAddress = it.stopAddress,
                    stopLat = it.stopLat,
                    stopLong = it.stopLong
            )
            stops.add(stop)
        }
        return stops
    }

    override fun addStop(service: Int, newStop: Stop) {
        val stopEntity = StopEntity(
                stopName = newStop.stopName,
                stopAddress = newStop.stopAddress,
                stopLat = newStop.stopLat,
                stopLong = newStop.stopLong
        )
        this.StopRepository.addStop(service,stopEntity)
    }

    override fun updateStop(s: UpdateStop) {
        val updateStopEntity = UpdateStopEntity(
                stopID = s.stopID,
                stopAddress = s.stopAddress,
                stopName = s.stopName,
                stopLat = s.stopLat,
                stopLong = s.stopLong
        )
        this.StopRepository.updateStop(updateStopEntity)
    }

    override fun archiveStop(stopID: Int) {
        this.StopRepository.archiveStop(stopID)
    }
}