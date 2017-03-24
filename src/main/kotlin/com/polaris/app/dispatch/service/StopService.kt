package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.Stop

interface StopService{
    fun retrieveStops(service: Int): List<Stop>
    fun addStop(service: Int, newStop:Stop)
}
