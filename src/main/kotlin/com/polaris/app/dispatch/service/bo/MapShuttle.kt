package com.polaris.app.dispatch.service.bo

import java.math.BigDecimal

data class MapShuttle(
        val shuttleID: Int,
        val shuttleName: String,
        val shuttleIconColor: String,
        val shuttleAssignmentID: Int,
        val shuttleLat: BigDecimal,
        val shuttleLong: BigDecimal,
        val shuttleStatus: String,
        val shuttleDriverID: Int,
        val shuttleDriverFName: String,
        val shuttleDriverLName: String
)