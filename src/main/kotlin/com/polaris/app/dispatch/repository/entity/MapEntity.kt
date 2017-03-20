package com.polaris.app.dispatch.repository.entity

import java.sql.Time

//Currently Unused
data class MapEntity(
        val shuttleID: Int,
        val shuttleName: String,
        val assignmentID: Int,
        val driverName: String,
        val shuttleLat: String,
        val shuttleLong: String,
        val stopName: String,
        val stopAddress: String,
        val stopLat: String,
        val stopLong: String,
        val stopArrive: Time,
        val stopDepart: Time,
        val stopArriveEst: Time,
        val stopDepartEst: Time
)
