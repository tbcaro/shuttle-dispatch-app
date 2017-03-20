package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.AssignmentRepository
import com.polaris.app.dispatch.repository.entity.AssignmentEntity
import com.polaris.app.dispatch.repository.entity.AssignmentStopEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Time

@Component
class AssignmentPgRepository(val db: JdbcTemplate): AssignmentRepository {
    override fun findAssignments(windowStart: Time, windowEnd: Time): List<AssignmentEntity> {
        val AssignmentEntities = db.query(
                "SELECT * FROM \"Assignment\" WHERE \"Assignment.TimeStamp\" > windowStart AND \"Assignment.TimeStamp\" < windowEnd",
                {
                    resultSet, rowNum -> AssignmentEntity(
                        resultSet.getInt("SerialID"),
                        resultSet.getTime("TimeStamp"),
                        resultSet.getString("RouteName"),
                        resultSet.getInt("DriverID"),
                        resultSet.getInt("ShuttleID")
                )
                }
        )
        return AssignmentEntities
    }

    override fun findAssignmentStops(assignID: Int): List<AssignmentStopEntity> {
        val AssignmentStopEntities = db.query(
                "SELECT * FROM \"Assignment\" LEFT OUTER JOIN \"AssignmentStops\" WHERE \"Assignment.SerialID\" = AssignID",
                {
                    resultSet, rowNum -> AssignmentStopEntity(
                        resultSet.getString("AssignmentStops.Name"),
                        resultSet.getString("AssignmentStops.Address"),
                        resultSet.getString("AssignmentStops.Latitude"),
                        resultSet.getString("AssignmentStops.Longitude"),
                        resultSet.getTime("AssignmentStops.TimeArrival"),
                        resultSet.getTime("AssignmentStops.TimeDepart"),
                        resultSet.getTime("AssignmentStops.EstArrival"),
                        resultSet.getTime("AssignmentStops.EstDepart")
                )
                }
        )
        return AssignmentStopEntities
    }

    override fun addAssignments(AssignmentEntities: List<AssignmentEntity>) {
        db.update(
                "INSERT INTO \"Assignments\" VALUES (AssignmentEntity.AssignmentID, AssignmentEntity.StartTime, AssignmentEntity.RouteName, AssignmentEntity.ShuttleID, AssignmentEntity.DriverID"
        )
    }

    //add override for function addAssignmentStops
}