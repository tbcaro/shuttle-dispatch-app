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
        val MapShuttleEntities = db.query(
                "SELECT * FROM shuttle_activity INNER JOIN shuttle ON (shuttle_activity.shuttleid = shuttle.\"ID\") WHERE shuttle.serviceid = ?;",
                arrayOf(service),
                {
                    resultSet, rowNum -> MapShuttleEntity(
                        resultSet.getInt("shuttle.\"ID\""),
                        resultSet.getString("shuttle.\"Name\""),
                        resultSet.getString("shuttle.iconcolor"),
                        resultSet.getInt("shuttle_activity.assignmentid"),
                        resultSet.getBigDecimal("shuttle_activity.latitude"),
                        resultSet.getBigDecimal("shuttle_activity.longitude"),
                        resultSet.getString("shuttle_activity.status"),
                        resultSet.getInt("shuttle_activity.driverid")
                )
                }

        )
        return MapShuttleEntities
    }

    override fun findShuttleDriver(shuttle: MapShuttleEntity): MapDriverEntity {
        val MapDriverEntities = db.query(
                "SELECT fname, lname FROM \"user\" WHERE \"ID\" = ?;",
                arrayOf(shuttle.shuttleDriverID),
                {
                    resultSet, rowNum -> MapDriverEntity(
                        resultSet.getInt("\"user\".\"ID\""),
                        resultSet.getString("\"user\".fname"),
                        resultSet.getString("\"user\".lname")
                )
                }
        )
        return MapDriverEntities[0]//Only one record should be returned since we are searching based on a unique ID
    }

    override fun findActiveAssignmentInfo(shuttle: MapShuttle): List<MapAssignStopEntity> {
        val MapAssignStopEntities = db.query(
                "SELECT * FROM assignment_stop INNER JOIN stop ON (assignment_stop.stopid = stop.\"ID\") WHERE assignment_stop.assignmentid = ?;",
                arrayOf(shuttle.shuttleAssignmentID),
                {
                    resultSet, rowNum -> MapAssignStopEntity(
                        resultSet.getString("stop.\"Name\""),
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
        return MapAssignStopEntities
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
