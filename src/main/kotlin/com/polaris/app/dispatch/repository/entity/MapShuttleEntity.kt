package com.polaris.app.dispatch.repository.entity

import java.math.BigDecimal

data class MapShuttleEntity(
    val shuttleID: Int,//Possibly unnecessary
    val shuttleName: String,//Displayed
    val shuttleIconColor: String,//Used for display
    val shuttleAssignmentID: Int,//Used to find further data
    val shuttleLat: BigDecimal,//Used for display
    val shuttleLong: BigDecimal,//Used for display
    val shuttleStatus: String,//Displayed
    val shuttleDriverID: Int//Used to find further data
)