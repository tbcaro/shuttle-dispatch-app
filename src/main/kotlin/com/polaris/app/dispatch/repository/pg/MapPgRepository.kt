package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.MapRepository
import com.polaris.app.dispatch.repository.entity.MapShuttleEntity
import com.polaris.app.dispatch.repository.entity.MapAssignmentEntity
import com.polaris.app.dispatch.repository.entity.MapAssignStopEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class MapPgRepository(val db: JdbcTemplate): MapRepository{
    override fun findActiveShuttles(service: Int): List<MapShuttleEntity> {
        val MapShuttleEntities = db.query(
                "SELECT * FROM \"ShuttleActivity\" LEFT OUTER JOIN \"Shuttle\" WHERE \"Shuttle.serviceid\" = service",
                {
                    resultSet, rowNum -> MapShuttleEntity(
                        resultSet.getInt("Shuttle.ID"),
                        resultSet.getString("Shuttle.Name"),
                        resultSet.getInt("Shuttle.IconColor"),
                        resultSet.getBigDecimal("ShuttleActivity.Latitude"),
                        resultSet.getBigDecimal("ShuttleActivity.Longitude"),
                        resultSet.getString("ShuttleActivity.Status")
                )
                }

        )
        return MapShuttleEntities
    }

    override fun findActiveAssignments(): List<MapAssignmentEntity> {
        val MapAssignmentEntities = db.query(
                "SELECT * FROM \"Assignment\" LEFT OUTER JOIN \"Driver\"",
                {
                    resultSet, rowNum -> MapAssignmentEntity(
                        resultSet.getInt("Assignment.SerialID"),
                        resultSet.getInt("Driver.ID"),
                        resultSet.getString("Driver.Name")
                )
                }
        )
        return MapAssignmentEntities
    }

    override fun findAssignStops(AssignID: Int): List<MapAssignStopEntity> {
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
    }
}
