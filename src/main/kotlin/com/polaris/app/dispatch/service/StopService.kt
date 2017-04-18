package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.NewStop
import com.polaris.app.dispatch.service.bo.Stop
import com.polaris.app.dispatch.service.bo.UpdateStop

interface StopService{
    fun retrieveStops(service: Int): List<Stop>
    fun addStop(newStop: NewStop): Int
    fun updateStop(s: UpdateStop): Int
    fun archiveStop(stopID: Int)
}
