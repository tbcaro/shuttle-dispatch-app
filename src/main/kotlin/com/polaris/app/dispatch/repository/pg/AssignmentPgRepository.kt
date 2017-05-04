package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import com.polaris.app.dispatch.repository.AssignmentRepository
import com.polaris.app.dispatch.repository.entity.*
import com.polaris.app.dispatch.service.bo.AssignmentUpdate
import com.polaris.app.dispatch.service.bo.NewAssignment
import com.polaris.app.dispatch.service.bo.NewAssignmentStop
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.sql.Types
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class AssignmentPgRepository(val db: JdbcTemplate): AssignmentRepository {
    override fun findAssignments(service: Int, date: LocalDate): List<AssignmentEntity> {
        val rows = db.queryForList(
                //Double check script to ensure database data is correct. At this time, database updates have not taken effect.
                "SELECT assignment.assignmentid, assignment.serviceid, assignment.startdate, assignment.starttime, assignment.routeid, route.\"Name\" AS routename, assignment.driverID, assignment.status, \"user\".fname, \"user\".lname, assignment.shuttleid, shuttle.\"Name\" " +
                        "FROM assignment " +
                        "INNER JOIN shuttle ON (assignment.shuttleid = shuttle.\"ID\") " +
                        "INNER JOIN \"user\" ON (assignment.driverid = \"user\".\"ID\") " +
                        "LEFT OUTER JOIN route ON (assignment.routeid = route.\"ID\") " +
                        "WHERE assignment.serviceid = ? AND startdate = ? AND assignment.isarchived = false AND shuttle.isarchived = false ORDER BY assignment.starttime;",
                service, Date.valueOf(date))

        val assignmentEntities = arrayListOf<AssignmentEntity>()
        rows.forEach{
            var routeid: Int? = null
            var starttime: LocalTime? = LocalTime.MIDNIGHT
            var startdate: LocalDate? = null
            var routename: String? = ""
            var shuttlename: String? = ""

            if (it["routeid"] != null)routeid = it["routeid"] as Int
            starttime = (it["starttime"]?.let { it as Time })?.toLocalTime()
            startdate = (it["startdate"]?.let { it as Date })?.toLocalDate()
            if (it["routename"] != null)routename = it["routename"] as String
            if (it["Name"] != null)shuttlename = it["Name"] as String

            val ae = AssignmentEntity(
                    assignmentID = it["assignmentid"] as Int,
                    serviceID = it["serviceid"] as Int,
                    startDate = startdate,
                    startTime = starttime,
                    routeID = routeid,
                    routeName = routename,
                    driverID = it["driverid"] as Int,
                    driverFName = it["fname"] as String,
                    driverLName = it["lname"] as String,
                    shuttleID = it["shuttleid"] as Int,
                    shuttleName = it["Name"] as String,
                    status = AssignmentState.valueOf(it["status"] as String)
            )
            assignmentEntities.add(ae)
        }
                /*{
                    resultSet, rowNum -> AssignmentEntity(
                        resultSet.getInt("assignmentid"),
                        resultSet.getInt("serviceid"),
                        resultSet.getTimestamp("startdate").toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp("starttime").toLocalDateTime().toLocalTime(),
                        resultSet.getInt("routeid"),
                        resultSet.getString("routename"),
                        resultSet.getInt("driverID"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        resultSet.getInt("shuttleid"),
                        resultSet.getString("Name"),
                        AssignmentState.valueOf(resultSet.getString("status"))
//                        AssignmentState.valueOf(resultSet.getString("status"))
                    )
                }
        )*/
        return assignmentEntities
    }

    override fun findAssignmentStops(assignID: Int): List<AssignmentStopEntity> {
        val rows = db.queryForList(
                "SELECT assignment_stop.assignment_stop_id, assignment_stop.assignmentid, assignment_stop.stopid, assignment_stop.\"Index\", stop.\"Name\", assignment_stop.address, " +
                        "assignment_stop.latitude, assignment_stop.longitude, assignment_stop.timeofarrival, " +
                        "assignment_stop.timeofdeparture, assignment_stop.estimatedtimeofarrival, assignment_stop.estimatedtimeofdeparture, " +
                        "stop.address AS stopAddress, stop.latitude AS stopLatitude, stop.longitude AS stopLongitude " +
                        "FROM assignment_stop LEFT OUTER JOIN stop ON (assignment_stop.stopid = stop.\"ID\") WHERE assignment_stop.assignmentid = ? ORDER BY \"Index\";",
                assignID
        )

        val assignmentStopEntities = arrayListOf<AssignmentStopEntity>()
        rows.forEach {
            var latitude = BigDecimal("0")
            var longitude = BigDecimal("0")
            var stopId: Int? = null
            var name: String = ""
            var address: String = ""

            if (it["stopLatitude"] != null)latitude = it["stopLatitude"] as BigDecimal
            else if (it["latitude"] != null) latitude = it["latitude"] as BigDecimal
            if (it["stopLongitude"] != null) longitude = it["stopLongitude"] as BigDecimal
            else if (it["longitude"] != null) longitude = it["longitude"] as BigDecimal
            if (it["stopID"] != null) stopId = it["stopID"] as Int?
            if (it["Name"] != null) name = it["Name"] as String
            if (it["stopAddress"] != null) address = it["stopAddress"] as String
            else if (it["address"] != null) address = it["address"] as String



            var arriveTime: LocalDateTime? = null
            var departTime: LocalDateTime? = null
            var estArriveTime: LocalDateTime? = null
            var estDepartTime: LocalDateTime? = null

            arriveTime = (it["timeofarrival"]?.let { it as Timestamp })?.toLocalDateTime()
            departTime = (it["timeofdeparture"]?.let { it as Timestamp })?.toLocalDateTime()
            estArriveTime = (it["estimatedtimeofarrival"]?.let { it as Timestamp })?.toLocalDateTime()
            estDepartTime = (it["estimatedtimeofdeparture"]?.let { it as Timestamp })?.toLocalDateTime()

            val assignmentStop = AssignmentStopEntity(
                    it["assignment_stop_id"] as Int,
                    it["assignmentid"] as Int,
                    stopId,
                    name,
                    it["Index"] as Int,
                    address,
                    latitude,
                    longitude,
                    arriveTime,
                    departTime,
                    estArriveTime,
                    estDepartTime
            )

            assignmentStopEntities.add(assignmentStop)
        }
        return assignmentStopEntities
    }

    override fun findDropShuttles(service: Int): List<AssignmentShuttleEntity> {
        val DropShuttles = db.query(
                "SELECT * FROM shuttle WHERE serviceid = ? AND isarchived = false AND isactive = true;",
                arrayOf(service),
                {
                    resultSet, rowNum -> AssignmentShuttleEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name")
                    )
                }
        )
        return DropShuttles
    }

    override fun findDropDrivers(service: Int): List<AssignmentDriverEntity> {
        val DropDrivers = db.query(
                "SELECT \"user\".\"ID\", \"user\".fname, \"user\".lname FROM \"user\" INNER JOIN driver ON (driver.\"ID\" = \"user\".\"ID\") WHERE driver.serviceid = ? AND driver.isarchived = false AND driver.isactive = true;",
                arrayOf(service),
                {
                    resultSet, rowNum -> AssignmentDriverEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname")
                    )
                }
        )
        return DropDrivers
    }

    override fun findDropRoutes(service: Int): List<AssignmentRouteEntity> {
        val DropRoutes = db.query(
                "SELECT * FROM route WHERE serviceid = ? AND isarchived = false;",
                arrayOf(service),
                {
                    resultSet, rowNum -> AssignmentRouteEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name")
                    )
                }
        )
        return DropRoutes
    }

    override fun findDropStops(service: Int): List<AssignmentStopDropEntity> {
        val DropStops = db.query(
                "SELECT * FROM stop WHERE serviceid = ? AND isarchived = false;",
                arrayOf(service),
                {
                    resultSet, rowNum -> AssignmentStopDropEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("address"),
                        resultSet.getBigDecimal("latitude"),
                        resultSet.getBigDecimal("longitude")
                    )
                }
        )
        return DropStops
    }

    override fun findDropRouteStops(routeID: Int): List<AssignmentRouteStopEntity> {
        val DropRouteStops = db.query(
                "SELECT * FROM route_stop INNER JOIN stop ON (route_stop.stopid = stop.\"ID\") WHERE routeid = ? ORDER BY \"Index\";",
                arrayOf(routeID),
                {
                    resultSet, rowNum -> AssignmentRouteStopEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getInt("Index"),
                        resultSet.getString("address"),
                        resultSet.getBigDecimal("latitude"),
                        resultSet.getBigDecimal("longitude")
                )
                }
        )
        return DropRouteStops
    }

    override fun addAssignment(a: NewAssignment): Int {
        var startTime: Time? = null
        var startDate: Date? = null

        a.startTime?.let { startTime = Time.valueOf(it) }
        a.startDate?.let { startDate = Date.valueOf(it) }

        if (a.routeID != null) {
            db.update(//TSH 4/2/2017: Added status and isarchived fields to query to properly assign them when creating a record
                    "INSERT INTO assignment (serviceid, driverid, shuttleid, routeid, startdate, starttime, status, isarchived) VALUES (?, ?, ?, ?, ?, ?, CAST(? AS assignment_status), false);",
                    a.serviceID,
                    a.driverID,
                    a.shuttleID,
                    a.routeID,
                    startDate,
                    startTime,
                    "SCHEDULED"
                    //arrayOf(a.serviceID, a.driverID, a.shuttleID, a.routeID, a.startDate, a.startTime, a.routeName)
            )
        }
        else(
            db.update(//TSH 4/2/2017: Added status and isarchived fields to query to properly assign them when creating a record
                    "INSERT INTO assignment (serviceid, driverid, shuttleid, routeid, startdate, starttime, status, isarchived, routename) VALUES (?, ?, ?, ?, ?, ?, CAST(? AS assignment_status), false, 'Custom Route');",
                    a.serviceID,
                    a.driverID,
                    a.shuttleID,
                    a.routeID,
                    startDate,
                    startTime,
                    "SCHEDULED"
                    //arrayOf(a.serviceID, a.driverID, a.shuttleID, a.routeID, a.startDate, a.startTime, a.routeName)
            )
        )

        val assignmentId = db.query(//Should only return the newly added assignment's assignmentid
                "SELECT assignmentid FROM assignment WHERE serviceid = ? AND driverid = ? AND startdate = ? AND starttime = ? AND isarchived = false;",
                arrayOf(a.serviceID, a.driverID, startDate, startTime),
                {
                    resultSet, rowNum -> AssignmentIDEntity(
                        resultSet.getInt("assignmentid")
                    )
                }

        )
        return assignmentId[0].assignmentid
    }

    override fun addAssignmentStops(assignmentID: Int?, assignmentStop: List<NewAssignmentStop>) {
        assignmentStop.forEach {
            var estArrive: Timestamp? = null
            var estDepart: Timestamp? = null

            if (it.stopArriveEst != null) estArrive = Timestamp.valueOf(it.stopArriveEst.atDate(LocalDate.of(1,1,1)))
            if (it.stopDepartEst != null) estDepart = Timestamp.valueOf(it.stopDepartEst.atDate(LocalDate.of(1,1,1)))

            db.update(
                    "INSERT INTO assignment_stop (assignmentid, estimatedtimeofarrival, estimatedtimeofdeparture, stopid, \"Index\", address, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                    assignmentID,
                    estArrive,
                    estDepart,
                    it.stopID,
                    it.stopIndex,
                    it.stopAddress,
                    it.stopLat,
                    it.stopLong
            )
        }
    }

    override fun updateAssignment(ua: AssignmentUpdate) {
        var startTime: Time? = null
        var startDate: Date? = null

        ua.startTime?.let { startTime = Time.valueOf(it) }
        ua.startDate?.let { startDate = Date.valueOf(it) }

        if (ua.routeID != null) {
            db.update(
                    "UPDATE assignment SET driverid = ?, shuttleid = ?, routeid = ?, starttime = ?, startdate = ? WHERE assignmentid = ?;",
                    ua.driverID,
                    ua.shuttleID,
                    ua.routeID,
                    startTime,
                    startDate,
                    ua.assignmentID
                    //arrayOf(ua.driverID, ua.shuttleID, ua.routeID, ua.startTime, ua.startDate, ua.routeName, ua.assignmentID)
            )
        }
        else {
            db.update(
                    "UPDATE assignment SET driverid = ?, shuttleid = ?, routeid = ?, starttime = ?, startdate = ?, routename = 'Custom Route' WHERE assignmentid = ?;",
                    ua.driverID,
                    ua.shuttleID,
                    ua.routeID,
                    startTime,
                    startDate,
                    ua.assignmentID
                    //arrayOf(ua.driverID, ua.shuttleID, ua.routeID, ua.startTime, ua.startDate, ua.routeName, ua.assignmentID)
            )
        }
    }

    override fun checkAssignment(assignmentID: Int?): AssignmentEntity {
        val assignment = db.query(
                "SELECT assignment.assignmentid, assignment.serviceid, assignment.startdate, assignment.starttime, assignment.routeid, assignment.routename, assignment.driverID, \"user\".fname, \"user\".lname, assignment.shuttleid, shuttle.\"ID\", assignment.status FROM assignment INNER JOIN shuttle ON (assignment.shuttleid = shuttle.\"ID\") INNER JOIN \"user\" ON (assignment.driverid = \"user\".\"ID\") WHERE assignment.assignmentid = ? AND assignment.isarchived = false AND shuttle.isarchived = false;",
                arrayOf(assignmentID),
                {
                    resultSet, rowNum -> AssignmentEntity(
                        resultSet.getInt("assignmentid"),
                        resultSet.getInt("serviceid"),
                        resultSet.getTimestamp("startdate").toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp("starttime").toLocalDateTime().toLocalTime(),
                        resultSet.getInt("routeid"),
                        resultSet.getString("routename"),
                        resultSet.getInt("driverID"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        resultSet.getInt("shuttleid"),
                        resultSet.getString("ID"),
                        status = AssignmentState.valueOf(resultSet.getString("status"))
                )
                }
        )
        return assignment[0]
    }

    override fun checkIndex(assignmentID: Int?): Int {
        val index = db.query(
                "SELECT \"Index\" FROM shuttle_activity WHERE assignmentid = ?;",
                arrayOf(assignmentID),{
                    resultSet, rowNum -> IndexEntity(
                        resultSet.getInt("Index")
                    )
                }
        )
        return index[0].index
    }

    override fun removeAssignmentStops(assignmentID: Int?, index: Int?) {
        db.update(
                "DELETE FROM assignment_stop WHERE assignmentid = ? AND \"Index\" > ?",
                assignmentID, index
        )
    }

    override fun archiveAssignment(assignmentID: Int) {
        db.update(
                "UPDATE assignment SET isarchived = true WHERE assignmentid = ?;",
                assignmentID
        )
    }

    override fun findAssignmentByAssignmentID(assignmentID: Int?): AssignmentEntity {
        val AssignmentEntities = db.query(
                //Double check script to ensure database data is correct. At this time, database updates have not taken effect.
                "SELECT assignment.assignmentid, assignment.serviceid, assignment.startdate, assignment.starttime, assignment.routeid, route.\"Name\" AS routename, assignment.driverID, assignment.status, \"user\".fname, \"user\".lname, assignment.shuttleid, shuttle.\"Name\" " +
                        "FROM assignment " +
                        "INNER JOIN shuttle ON (assignment.shuttleid = shuttle.\"ID\") " +
                        "INNER JOIN \"user\" ON (assignment.driverid = \"user\".\"ID\") " +
                        "LEFT OUTER JOIN route ON (assignment.routeid = route.\"ID\") " +
                        "WHERE assignment.assignmentID = ? AND assignment.isarchived = false AND shuttle.isarchived = false;",
                arrayOf(assignmentID),
                {
                    resultSet, rowNum -> AssignmentEntity(
                        resultSet.getInt("assignmentid"),
                        resultSet.getInt("serviceid"),
                        resultSet.getTimestamp("startdate").toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp("starttime").toLocalDateTime().toLocalTime(),
                        resultSet.getInt("routeid"),
                        resultSet.getString("routename"),
                        resultSet.getInt("driverID"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        resultSet.getInt("shuttleid"),
                        resultSet.getString("Name"),
                        AssignmentState.valueOf(resultSet.getString("status"))
                        //AssignmentState.valueOf(resultSet.getString("status"))
                )
                }
        )
        return AssignmentEntities[0]
    }

    override fun findStopIndex(assignmentStopID: Int): Int {
        val index = db.query(
                "SELECT \"Index\" FROM assignment_stop WHERE assignment_stop_id = ?;",
                arrayOf(assignmentStopID),{
                    resultSet, rowNum -> IndexEntity(
                        resultSet.getInt("Index")
                    )
                }
        )
        return index[0].index
    }

    override fun findStop(assignmentStopID: Int): AssignmentStopEntity {
        val rows = db.queryForList(
                "SELECT * FROM assignment_stop INNER JOIN stop ON (assignment_stop.stopid = stop.\"ID\") WHERE assignment_stop_id = ?;",
                assignmentStopID
        )

        val assignmentStopEntities = arrayListOf<AssignmentStopEntity>()
        rows.forEach {
            var latitude = it["latitude"] as BigDecimal
            var longitude = it["longitude"] as BigDecimal
            var arriveTime: LocalDateTime? = null
            var departTime: LocalDateTime? = null
            var estArriveTime: LocalDateTime? = null
            var estDepartTime: LocalDateTime? = null

            arriveTime = (it["timeofarrival"]?.let { it as Timestamp })?.toLocalDateTime()
            departTime = (it["timeofdeparture"]?.let { it as Timestamp })?.toLocalDateTime()
            estArriveTime = (it["estimatedtimeofarrival"]?.let { it as Timestamp })?.toLocalDateTime()
            estDepartTime = (it["estimatedtimeofdeparture"]?.let { it as Timestamp })?.toLocalDateTime()

            val assignmentStop = AssignmentStopEntity(
                    it["assignment_stop_id"] as Int,
                    it["assignmentid"] as Int,
                    it["stopid"] as Int,
                    it["Name"] as String,
                    it["Index"] as Int,
                    it["address"] as String,
                    latitude,
                    longitude,
                    arriveTime,
                    departTime,
                    estArriveTime,
                    estDepartTime
            )

            assignmentStopEntities.add(assignmentStop)
        }
        return assignmentStopEntities[0]
    }

    override fun checkForAssignment(a: NewAssignment): Boolean {
        var startTime: Time? = null
        var startDate: Date? = null

        a.startTime?.let { startTime = Time.valueOf(it) }
        a.startDate?.let { startDate = Date.valueOf(it) }

        val prevAssigns = db.query(//Should only return the newly added assignment's assignmentid
                "SELECT assignmentid FROM assignment WHERE serviceid = ? AND driverid = ? AND startdate = ? AND starttime = ? AND isarchived = false;",
                arrayOf(a.serviceID, a.driverID, startDate, startTime),
                {
                    resultSet, rowNum -> AssignmentIDEntity(
                        resultSet.getInt("assignmentid")
                )
                }

        )
        return prevAssigns.size == 0
    }
}