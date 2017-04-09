package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.AssignmentRepository
import com.polaris.app.dispatch.repository.entity.*
import com.polaris.app.dispatch.service.bo.NewAssignment
import com.polaris.app.dispatch.service.bo.NewAssignmentStop
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class AssignmentPgRepository(val db: JdbcTemplate): AssignmentRepository {
    override fun findAssignments(service: Int, date: LocalDate): List<AssignmentEntity> {
        val AssignmentEntities = db.query(
                //Double check script to ensure database data is correct. At this time, database updates have not taken effect.
                "SELECT assignment.assignmentid, assignment.serviceid, assignment.startdate, assignment.starttime, assignment.routeid, assignment.routename, assignment.driverID, \"user\".fname, \"user\".lname, assignment.shuttleid, shuttle.\"ID\" FROM assignment INNER JOIN shuttle ON (assignment.shuttleid = shuttle.\"ID\") INNER JOIN \"user\" ON (assignment.driverid = \"user\".\"ID\") WHERE assignment.serviceid = ? AND startdate = ? AND assignment.isarchived = false AND shuttle.isarchived = false;",
                arrayOf(service, date),
                {
                    resultSet, rowNum -> AssignmentEntity(
                        resultSet.getInt("assignment.assignmentid"),
                        resultSet.getInt("assignment.serviceid"),
                        resultSet.getTimestamp("assignment.startdate").toLocalDateTime().toLocalDate(),
                        resultSet.getTimestamp("assignment.starttime").toLocalDateTime().toLocalTime(),
                        resultSet.getInt("assignment.routeid"),
                        resultSet.getString("assignment.routename"),
                        resultSet.getInt("assignment.driverID"),
                        resultSet.getString("\"user\".fname"),
                        resultSet.getString("\"user\".lname"),
                        resultSet.getInt("assignment.shuttleid"),
                        resultSet.getString("shuttle.\"ID\"")
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
                        resultSet.getInt("assignment_stop.assignment_stop_id"),
                        resultSet.getInt("assignment_stop.assignmentid"),
                        resultSet.getInt("assignment_stop.stopid"),
                        resultSet.getString("stop.\"Name\""),
                        resultSet.getInt("assignment_stop.\"Index\""),
                        resultSet.getString("assignment_stop.address"),
                        resultSet.getBigDecimal("assignment_stop.latitude"),
                        resultSet.getBigDecimal("assignment_stop.longitude"),
                        resultSet.getTimestamp("assignment_stop.timeofarrival").toLocalDateTime(),
                        resultSet.getTimestamp("assignment_stop.timeofdeparture").toLocalDateTime(),
                        resultSet.getTimestamp("assignment_stop.estimatedtimeofarrival").toLocalDateTime(),
                        resultSet.getTimestamp("assignment_stop.estimatedtimeofdeparture").toLocalDateTime()
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
                        resultSet.getInt("\"ID\""),
                        resultSet.getString("\"Name\"")
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
                        resultSet.getInt("\"user\".\"ID\""),
                        resultSet.getString(" \"user\".fname"),
                        resultSet.getString("\"user\".lname")
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
                        resultSet.getInt("\"ID\""),
                        resultSet.getString("\"Name\"")
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
                        resultSet.getInt("\"ID\""),
                        resultSet.getString("\"Name\""),
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
                        resultSet.getInt("stop.\"ID\""),
                        resultSet.getString("stop.\"Name\""),
                        resultSet.getInt("route_stop.\"Index\""),
                        resultSet.getString("stop.address"),
                        resultSet.getBigDecimal("stop.latitude"),
                        resultSet.getBigDecimal("stop.longitude")
                )
                }
        )
        return DropRouteStops
    }

    override fun addAssignment(a: NewAssignment): Int {
        db.update(//TSH 4/2/2017: Added status and isarchived fields to query to properly assign them when creating a record
                "INSERT INTO assignment (serviceid, driverid, shuttleid, routeid, startdate, starttime, routename, status, isarchived) VALUES (?, ?, ?, ?, ?, ?, ?, 'SCHEDULED', false);",
                arrayOf(a.serviceID, a.driverID, a.shuttleID, a.routeID, a.startDate, a.startTime, a.routeName)
        )

        val assignmentid = db.query(//Should only return the newly added assignment's assignmentid
                "SELECT assignmentid FROM assignment WHERE serviceid = ? AND driverid = ? AND startdate = ? AND starttime = ? AND isarchived = false;",
                arrayOf(a.serviceID, a.driverID, a.startDate, a.startTime),
                {
                    resultSet, rowNum -> AssignmentIDEntity(
                        resultSet.getInt("assignmentid")
                    )
                }

        )
        return assignmentid[0].assignmentid
    }

    override fun addAssignmentStops(assignmentID: Int, assignmentStop: List<NewAssignmentStop>) {
        assignmentStop.forEach {
            db.update(
                    "INSERT INTO assignment_stop (assignmentid, estimatedtimeofarrival, estimatedtimeofdeparture, stopid, \"Index\", address, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?);",
                    arrayOf(assignmentID, it.stopArriveEst, it.stopDepartEst, it.stopID, it.stopAddress, it.stopLat, it.stopLong)
            )
        }
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