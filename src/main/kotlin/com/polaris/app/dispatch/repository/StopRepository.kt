package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.StopEntity
import com.polaris.app.dispatch.repository.entity.UpdateStopEntity

interface StopRepository {
    fun findStops(service: Int):List<StopEntity>
    fun addStop(service:Int, stop: StopEntity)
    fun updateStop(s: UpdateStopEntity)
    fun archiveStop(stopID: Int)
}