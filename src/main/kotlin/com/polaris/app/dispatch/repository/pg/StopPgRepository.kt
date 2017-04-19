package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.StopRepository
import com.polaris.app.dispatch.repository.entity.NewStopEntity
import com.polaris.app.dispatch.repository.entity.StopEntity
import com.polaris.app.dispatch.repository.entity.StopIDEntity
import com.polaris.app.dispatch.repository.entity.UpdateStopEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Component
import java.math.BigDecimal

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

    override fun addStop(service: Int, stop: NewStopEntity): Int {
        val lat = stop.stopLat!!.minus(stop.stopLat.mod(BigDecimal.valueOf(.000001)))
        val long = stop.stopLong!!.minus(stop.stopLong.mod(BigDecimal.valueOf(.000001)))
        db.update(
                "INSERT INTO stop (serviceid, \"Name\", address, latitude, longitude, isarchived) VALUES (?, ?, ?, ?, ?, false)",
                        service, stop.stopName, stop.stopAddress, lat, long
        )

        // TODO: fix select statement to get stopID
        // Issue seems to be rooted in the database and the app saving lat and long to different degrees of accuracy


        val stopID = db.query(
                "SELECT \"ID\" FROM stop WHERE serviceid = ? AND \"Name\" = ? AND address = ? AND latitude = ? AND longitude = ? AND isarchived = false",
                arrayOf(service, stop.stopName, stop.stopAddress, lat, long), {
            resultSet, rowNum -> StopIDEntity(
                resultSet.getInt("ID")
        )
        }
        )
        return stopID[0].stopID
        /*var keyHolder: KeyHolder
        keyHolder.
        val psc: PreparedStatementCreator

        db.update(, keyHolder)

        )*/
    }

    override fun updateStop(s: UpdateStopEntity): Int {
        db.update(
                "UPDATE stop SET \"Name\" = ?, address = ?, latitude = ?, longitude = ? WHERE \"ID\" = ?;",
                s.stopName, s.stopAddress, s.stopLat, s.stopLong, s.stopID
        )
        val stopID = db.query(
                "SELECT \"ID\" FROM stop WHERE serviceid = ? AND \"Name\" = ? AND address = ? AND latitude = ? AND longitude = ? AND isarchived = false",
                arrayOf(s.serviceID, s.stopName, s.stopAddress, s.stopLat, s.stopLong), {
            resultSet, rowNum -> StopIDEntity(
                resultSet.getInt("ID")
        )
        }
        )
        return stopID[0].stopID
    }

    override fun archiveStop(stopID: Int) {
        db.update(
                "UPDATE stop SET isarchived = true WHERE \"ID\" = ?",
                stopID
        )
    }
}
