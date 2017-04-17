package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.StopRepository
import com.polaris.app.dispatch.repository.entity.NewStopEntity
import com.polaris.app.dispatch.repository.entity.StopEntity
import com.polaris.app.dispatch.repository.entity.UpdateStopEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class StopPgRepository(val db: JdbcTemplate): StopRepository {
    override fun findStops(service: Int): List<StopEntity> {
        val StopEntities = db.query(
                "SELECT * FROM stop WHERE serviceid = ? AND isarchived = false ORDER BY \"Name\"",
                arrayOf(service),
                {
                    resultSet, rowNum -> StopEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Address"),
                        resultSet.getBigDecimal("Latitude"),
                        resultSet.getBigDecimal("Longitude")
                    )
                }
        )
        return StopEntities
    }

    override fun addStop(service: Int, stop: NewStopEntity) {
        db.update(
                "INSERT INTO stop (serviceid, \"Name\", address, latitude, longitude, isarchived) VALUES (?, ?, ?, ?, ?, false)",
                        service, stop.stopName, stop.stopAddress, stop.stopLat, stop.stopLong
        )
    }

    override fun updateStop(s: UpdateStopEntity) {
        db.update(
                "UPDATE stop SET \"Name\" = ?, address = ?, latitude = ?, longitude = ? WHERE \"ID\" = ?;",
                s.stopName, s.stopAddress, s.stopLat, s.stopLong, s.stopID
        )
    }

    override fun archiveStop(stopID: Int) {
        db.update(
                "UPDATE stop SET isarchived = true WHERE \"ID\" = ?",
                stopID
        )
    }
}
