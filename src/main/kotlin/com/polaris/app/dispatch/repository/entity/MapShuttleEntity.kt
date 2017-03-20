package com.polaris.app.dispatch.repository.entity

data class MapShuttleEntity(
    val ShuttleID: Int,
    val ShuttleName: String,
    val ShuttleIconColor: Int,
    val ShuttleLat: String,
    val ShuttleLong: String,
    val ShuttleStatus: String
)