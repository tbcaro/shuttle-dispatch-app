package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.NewStopEntity
import com.polaris.app.dispatch.repository.entity.StopEntity
import com.polaris.app.dispatch.repository.entity.UpdateStopEntity

interface StopRepository {
    fun findStops(service: Int):List<StopEntity>
    fun addStop(service:Int, stop: NewStopEntity)
    fun updateStop(s: UpdateStopEntity)
    fun archiveStop(stopID: Int)
}