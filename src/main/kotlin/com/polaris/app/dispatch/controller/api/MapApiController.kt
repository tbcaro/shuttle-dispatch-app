package com.polaris.app.dispatch.controller.api

import com.polaris.app.dispatch.controller.adapter.AssignmentReport
import com.polaris.app.dispatch.controller.adapter.ShuttleActivityDetailsAdapter
import com.polaris.app.dispatch.controller.adapter.AssignmentStopAdapter
import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalTime

@RestController
@RequestMapping("/api")
class MapApiController {

    @RequestMapping("/fetchAllShuttleActivity")
    fun fetchAllShuttleActivity(
            serviceCode: String?
    ) : ResponseEntity<List<ShuttleActivityDetailsAdapter>> {

        if (serviceCode.isNullOrBlank()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val listOfDetails = arrayListOf<ShuttleActivityDetailsAdapter>()

        // TBC : TODO : Implement calls to services to retrieve data to populate list of adapters

        return ResponseEntity(listOfDetails, HttpStatus.OK)
    }
}