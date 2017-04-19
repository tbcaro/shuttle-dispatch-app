package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.MapRepository
import com.polaris.app.dispatch.repository.entity.MapShuttleEntity
//import com.polaris.app.dispatch.repository.entity.MapAssignmentEntity
import com.polaris.app.dispatch.repository.entity.MapAssignStopEntity
import com.polaris.app.dispatch.repository.entity.MapDriverEntity
import com.polaris.app.dispatch.service.bo.MapShuttle
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime

@Component
class MapPgRepository(val db: JdbcTemplate): MapRepository{
    override fun findActiveShuttles(service: Int): List<MapShuttleEntity> {
        val mapShuttleEntities = db.query(
                "SELECT * FROM shuttle_activity INNER JOIN shuttle ON (shuttle_activity.shuttleid = shuttle.\"ID\") WHERE shuttle.serviceid = ?",
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
                "SELECT \"ID\", fname, lname FROM \"user\" WHERE \"ID\" = ?",
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
        val rows = db.queryForList(
                "SELECT assignment_stop.assignment_stop_id, assignment_stop.stopid, assignment_stop.\"Index\", stop.\"Name\", assignment_stop.address, " +
                        "assignment_stop.latitude, assignment_stop.longitude, assignment_stop.timeofarrival, " +
                        "assignment_stop.timeofdeparture, assignment_stop.estimatedtimeofarrival, assignment_stop.estimatedtimeofdeparture, " +
                        "stop.address AS stopAddress, stop.latitude AS stopLatitude, stop.longitude AS stopLongitude " +
                        "FROM assignment_stop LEFT OUTER JOIN stop ON (assignment_stop.stopid = stop.\"ID\") WHERE assignment_stop.assignmentid = ? ORDER BY \"Index\";",
                shuttle.shuttleAssignmentID
        )

        val mapAssignStopEntities = arrayListOf<MapAssignStopEntity>()
        rows.forEach{
            var latitude = BigDecimal("0")
            var longitude = BigDecimal("0")
            var stopId: Int? = null
            var name: String = ""
            var address: String = ""

            if (it["latitude"] != null) latitude = it["latitude"] as BigDecimal
            else if (it["stopLatitude"] != null)latitude = it["stopLatitude"] as BigDecimal
            if (it["longitude"] != null) longitude = it["longitude"] as BigDecimal
            else if (it["stopLongitude"] != null) longitude = it["stopLongitude"] as BigDecimal
            if (it["stopID"] != null) stopId = it["stopID"] as Int?
            if (it["Name"] != null) name = it["Name"] as String
            if (it["address"] != null) address = it["address"] as String
            else if (it["stopAddress"] != null) address = it["stopAddress"] as String

            var arriveTime: LocalDateTime? = null
            var departTime: LocalDateTime? = null
            var estArriveTime: LocalDateTime? = null
            var estDepartTime: LocalDateTime? = null

            arriveTime = (it["timeofarrival"]?.let { it as Timestamp })?.toLocalDateTime()
            departTime = (it["timeofdeparture"]?.let { it as Timestamp })?.toLocalDateTime()
            estArriveTime = (it["estimatedtimeofarrival"]?.let { it as Timestamp })?.toLocalDateTime()
            estDepartTime = (it["estimatedtimeofdeparture"]?.let { it as Timestamp })?.toLocalDateTime()

            val assignmentStop = MapAssignStopEntity(
                    it["assignment_stop_id"] as Int,
                    stopId,
                    it["Index"] as Int,
                    name,
                    address,
                    latitude,
                    longitude,
                    arriveTime,
                    departTime,
                    estArriveTime,
                    estDepartTime
            )

            mapAssignStopEntities.add(assignmentStop)
        }
        /*
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
                }*/

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
