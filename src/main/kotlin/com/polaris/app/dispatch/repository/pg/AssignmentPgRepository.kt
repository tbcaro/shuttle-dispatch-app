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
import java.sql.Date
import java.sql.Time
import java.sql.Types
import java.text.DateFormat
import java.time.LocalDate

@Component
class AssignmentPgRepository(val db: JdbcTemplate): AssignmentRepository {
    override fun findAssignments(service: Int, date: LocalDate): List<AssignmentEntity> {
        val AssignmentEntities = db.query(
                //Double check script to ensure database data is correct. At this time, database updates have not taken effect.
                "SELECT assignment.assignmentid, assignment.serviceid, assignment.startdate, assignment.starttime, assignment.routeid, assignment.routename, assignment.driverID, assignment.status, \"user\".fname, \"user\".lname, assignment.shuttleid, shuttle.\"ID\" FROM assignment INNER JOIN shuttle ON (assignment.shuttleid = shuttle.\"ID\") INNER JOIN \"user\" ON (assignment.driverid = \"user\".\"ID\") WHERE assignment.serviceid = ? AND startdate = ? AND assignment.isarchived = false AND shuttle.isarchived = false;",
                arrayOf(service, Date.valueOf(date)),
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
                        AssignmentState.valueOf(resultSet.getString("status"))
//                        AssignmentState.valueOf(resultSet.getString("status"))
                    )
                }
        )
        return AssignmentEntities
    }

    override fun findAssignmentStops(assignID: Int): List<AssignmentStopEntity> {
        val AssignmentStopEntities = db.query(
                "SELECT * FROM assignment_stop INNER JOIN stop ON (assignment_stop.stopid = stop.\"ID\") WHERE assignmentid = ?;",
                arrayOf(assignID),
                {
                    resultSet, rowNum -> AssignmentStopEntity(
                        resultSet.getInt("assignment_stop_id"),
                        resultSet.getInt("assignmentid"),
                        resultSet.getInt("stopid"),
                        resultSet.getString("Name"),
                        resultSet.getInt("Index"),
                        resultSet.getString("address"),
                        resultSet.getBigDecimal("latitude"),
                        resultSet.getBigDecimal("longitude"),
                        resultSet.getTimestamp("timeofarrival").toLocalDateTime(),
                        resultSet.getTimestamp("timeofdeparture").toLocalDateTime(),
                        resultSet.getTimestamp("estimatedtimeofarrival").toLocalDateTime(),
                        resultSet.getTimestamp("estimatedtimeofdeparture").toLocalDateTime()
                    )
                }
        )
        return AssignmentStopEntities
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
                "SELECT * FROM route_stop INNER JOIN stop ON (route_stop.stopid = stop.\"ID\") WHERE routeid = ?;",
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
        val assignmentId = db.update(//TSH 4/2/2017: Added status and isarchived fields to query to properly assign them when creating a record
                "INSERT INTO assignment (serviceid, driverid, shuttleid, routeid, startdate, starttime, status, isarchived) VALUES (?, ?, ?, ?, ?, ?, CAST(? AS assignment_status), false);",
                arrayOf(a.serviceID, a.driverID, a.shuttleID, a.routeID, Date.valueOf(a.startDate), Time.valueOf(a.startTime), "SCHEDULED")
                //arrayOf(a.serviceID, a.driverID, a.shuttleID, a.routeID, a.startDate, a.startTime, a.routeName)
        )

//        val assignmentid = db.query(//Should only return the newly added assignment's assignmentid
//                "SELECT assignmentid FROM assignment WHERE serviceid = ? AND driverid = ? AND startdate = ? AND starttime = ? AND isarchived = false;",
//                arrayOf(a.serviceID, a.driverID, a.startDate, a.startTime),
//                {
//                    resultSet, rowNum -> AssignmentIDEntity(
//                        resultSet.getInt("assignmentid")
//                    )
//                }
//
//        )
        return assignmentId
    }

    override fun addAssignmentStops(assignmentID: Int, assignmentStop: List<NewAssignmentStop>) {
        assignmentStop.forEach {
            db.update(
                    "INSERT INTO assignment_stop (assignmentid, estimatedtimeofarrival, estimatedtimeofdeparture, stopid, \"Index\", address, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                    arrayOf(assignmentID, it.stopArriveEst, it.stopDepartEst, it.stopID, it.stopIndex, it.stopAddress, it.stopLat, it.stopLong)
            )
        }
    }

    override fun updateAssignment(ua: AssignmentUpdate) {
        db.update(
                "UPDATE assignment SET driverid = ? AND shuttleid = ? AND routeid = ? AND starttime = ? AND startdate = ? WHERE assignmentid = ?;",
                arrayOf(ua.driverID, ua.shuttleID, ua.routeID, ua.startTime, ua.startDate, ua.assignmentID)
                //arrayOf(ua.driverID, ua.shuttleID, ua.routeID, ua.startTime, ua.startDate, ua.routeName, ua.assignmentID)
        )
    }

    override fun checkAssignment(assignmentID: Int): AssignmentEntity {
        val assignment = db.query(
                "SELECT assignment.assignmentid, assignment.serviceid, assignment.startdate, assignment.starttime, assignment.routeid, assignment.routename, assignment.driverID, \"user\".fname, \"user\".lname, assignment.shuttleid, shuttle.\"ID\" FROM assignment INNER JOIN shuttle ON (assignment.shuttleid = shuttle.\"ID\") INNER JOIN \"user\" ON (assignment.driverid = \"user\".\"ID\") WHERE assignment.assignmentid = ? AND assignment.isarchived = false AND shuttle.isarchived = false;",
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

    override fun checkIndex(assignmentID: Int): Int {
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

    override fun removeAssignmentStops(assignmentID: Int, index: Int) {
        db.update(
                "DELETE FROM assignment_stop WHERE assignmentid = ? AND \"Index\" > ?"
        )
    }

    override fun archiveAssignment(assignmentID: Int) {
        db.update(
                "UPDATE assignment SET isarchived = true WHERE assignmentid = ?;",
                arrayOf(assignmentID)
        )
    }

    override fun startTransaction() {
        db.update(
                "BEGIN;"
        )
    }

    override fun endTransaction() {
        db.update(
                "COMMIT;"
        )
    }
}