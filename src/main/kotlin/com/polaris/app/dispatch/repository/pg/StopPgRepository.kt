package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.StopRepository
import com.polaris.app.dispatch.repository.entity.StopEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class StopPgRepository(val db: JdbcTemplate): StopRepository {
    override fun findStops(service: Int): List<StopEntity> {
        val StopEntities = db.query(
                "SELECT * FROM stop WHERE serviceid = ? AND isarchived = false",
                arrayOf(service),
                {
                    resultSet, rowNum -> StopEntity(
                        resultSet.getString("Name"),
                        resultSet.getString("Address"),
                        resultSet.getBigDecimal("Latitude"),
                        resultSet.getBigDecimal("Longitude")
                    )
                }
        )
        return StopEntities
    }

    override fun addStop(service: Int, stop: StopEntity) {
        db.update(
                "INSERT INTO stop (serviceid, \"Name\", address, latitude, longitude, isarchived) VALUES (?, ?, ?, ?, ?, false)",
                        arrayOf(service, stop.stopName, stop.stopAddress, stop.stopLat, stop.stopLong)
        )
    }

}
