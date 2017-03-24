package com.polaris.app.dispatch.repository

import com.polaris.app.dispatch.repository.entity.StopEntity

interface StopRepository {
    fun findStops(service: Int):List<StopEntity>
    fun addStop(service:Int, stop: StopEntity)

}