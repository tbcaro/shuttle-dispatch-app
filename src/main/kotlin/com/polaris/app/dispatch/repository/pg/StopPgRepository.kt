package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.StopRepository
import com.polaris.app.dispatch.repository.entity.StopEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class StopPgRepository(val db: JdbcTemplate): StopRepository {
    override fun findStops(service: Int): List<StopEntity> {
        val StopEntities = db.query(
                "SELECT * FROM \"Stop\" WHERE \"ServiceID\" = service",
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
                "INSERT INTO \"Stop\" (\"serviceid\", \"Name\", \"address\", \"latitude\", \"longitude\", \"isarchived\") VALUES (service, stop.Name, stop.Address, stop.Latitude, stop.Longitude, false)"
        )
    }

}
