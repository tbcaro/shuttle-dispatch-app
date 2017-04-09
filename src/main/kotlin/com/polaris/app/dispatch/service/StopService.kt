package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.Stop
import com.polaris.app.dispatch.service.bo.UpdateStop

interface StopService{
    fun retrieveStops(service: Int): List<Stop>
    fun addStop(service: Int, newStop:Stop)
    fun updateStop(s: UpdateStop)
    fun archiveStop(stopID: Int)
}
