package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.MapRepository
import com.polaris.app.dispatch.repository.entity.MapShuttleEntity
//import com.polaris.app.dispatch.repository.entity.MapAssignmentEntity
import com.polaris.app.dispatch.repository.entity.MapAssignStopEntity
import com.polaris.app.dispatch.repository.entity.MapDriverEntity
import com.polaris.app.dispatch.service.bo.MapShuttle
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class MapPgRepository(val db: JdbcTemplate): MapRepository{
    override fun findActiveShuttles(service: Int): List<MapShuttleEntity> {
        val mapShuttleEntities = db.query(
                "SELECT * FROM shuttle_activity INNER JOIN shuttle ON (shuttle_activity.shuttleid = shuttle.\"ID\") WHERE shuttle.serviceid = ?;",
                arrayOf(service),
                {
                    resultSet, rowNum -> MapShuttleEntity(
                        resultSet.getInt("shuttleid"),
                        resultSet.getString("Name"),
                        resultSet.getString("iconcolor"),
                        resultSet.getInt("assignmentid"),
                        resultSet.getBigDecimal("latitude"),
                        resultSet.getBigDecimal("longitude"),
                        resultSet.getString("status"),
                        resultSet.getInt("driverid"),
                        resultSet.getInt("index"),
                        resultSet.getBigDecimal("heading")
                )
                }

        )
        return mapShuttleEntities
    }

    override fun findShuttleDriver(shuttle: MapShuttleEntity): List<MapDriverEntity> {
        val mapDriverEntities = db.query(
                "SELECT \"ID\", fname, lname FROM \"user\" WHERE \"ID\" = ?;",
                arrayOf(shuttle.shuttleDriverID),
                {
                    resultSet, rowNum -> MapDriverEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname")
                )
                }
        )
        return mapDriverEntities
    }

    override fun findActiveAssignmentInfo(shuttle: MapShuttle): List<MapAssignStopEntity> {
        val mapAssignStopEntities = db.query(
                "SELECT * FROM assignment_stop INNER JOIN stop ON (assignment_stop.stopid = stop.\"ID\") WHERE assignment_stop.assignmentid = ?;",
                arrayOf(shuttle.shuttleAssignmentID),
                {
                    resultSet, rowNum -> MapAssignStopEntity(
                        resultSet.getInt("assignment_stop_id"),
                        resultSet.getInt("stopid"),
                        resultSet.getInt("index"),
                        resultSet.getString("Name"),
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
        return mapAssignStopEntities
    }

    /*override fun findAssignStops(AssignID: Int): List<MapAssignStopEntity> {
        val AssignmentStopEntities = db.query(
                "SELECT * FROM \"Assignment\" LEFT OUTER JOIN \"AssignmentStops\" WHERE \"Assignment.SerialID\" = AssignID",
                {
                    resultSet, rowNum -> MapAssignStopEntity(
                        resultSet.getString("AssignmentStops.Name"),
                        resultSet.getString("AssignmentStops.Address"),
                        resultSet.getBigDecimal("AssignmentStops.Latitude"),
                        resultSet.getBigDecimal("AssignmentStops.Longitude"),
                        resultSet.getTimestamp("AssignmentStops.TimeArrival").toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp("AssignmentStops.TimeDepart").toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp("AssignmentStops.EstArrival").toLocalDateTime().toLocalTime(),
                        resultSet.getTimestamp("AssignmentStops.EstDepart").toLocalDateTime().toLocalTime()
                )
                }

        )
        return AssignmentStopEntities
    }*/
}
