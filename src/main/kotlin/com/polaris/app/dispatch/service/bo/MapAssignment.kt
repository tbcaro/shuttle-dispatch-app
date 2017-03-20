package com.polaris.app.dispatch.service.bo

data class MapAssignment(
        val AssignmentID: Int,
        val DriverID: Int,
        val DriverName: String,
        val Stops: List<MapAssignmentStop>
)