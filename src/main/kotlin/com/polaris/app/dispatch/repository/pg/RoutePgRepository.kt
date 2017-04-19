package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.RouteRepository
import com.polaris.app.dispatch.repository.entity.RouteEntity
import com.polaris.app.dispatch.repository.entity.RouteIDEntity
import com.polaris.app.dispatch.repository.entity.RouteStopEntity
import com.polaris.app.dispatch.repository.entity.StopEntity
import com.polaris.app.dispatch.service.bo.NewRoute
import com.polaris.app.dispatch.service.bo.Route
import com.polaris.app.dispatch.service.bo.UpdateRoute
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class RoutePgRepository(val db: JdbcTemplate): RouteRepository {
    override fun findRoutes(serviceID: Int): List<RouteEntity> {
        val routeEntities = db.query(
                "SELECT * FROM route WHERE serviceid = ? AND isarchived = false;",
                arrayOf(serviceID),{
                    resultSet, rowNum -> RouteEntity(
                        resultSet.getInt("serviceid"),
                        resultSet.getInt("ID"),
                        resultSet.getString("Name")
                    )
                }
        )
        return routeEntities
    }

    override fun findRouteStops(route: RouteEntity): List<RouteStopEntity> {
        val routeStopEntities = db.query(
                "SELECT * FROM route_stop INNER JOIN stop ON (route_stop.stopid = stop.\"ID\") WHERE routeid = ? ORDER BY \"Index\";",
                arrayOf(route.routeID),{
                    resultSet, rowNum -> RouteStopEntity(
                        resultSet.getInt("routeid"),
                        resultSet.getInt("stopid"),
                        resultSet.getInt("Index"),
                        resultSet.getString("Name"),
                        resultSet.getString("address"),
                        resultSet.getBigDecimal("latitude"),
                        resultSet.getBigDecimal("longitude")
                    )
                }
        )
        return routeStopEntities
    }

    override fun insertRoute(route: NewRoute): Int {
        db.update(
                "INSERT INTO route (serviceid, \"Name\", isarchived) VALUES (?, ?, false);",
                route.serviceID, route.routeName
        )
        val routeID = db.query(
                "SELECT * FROM route WHERE serviceid = ? AND \"Name\" = ? AND isarchived = false",
        arrayOf(route.serviceID, route.routeName),{
            resultSet, rowNum -> RouteIDEntity(
                resultSet.getInt("ID")
        )
        }
        )
        return routeID[0].routeID
    }

    override fun insertRouteStop(routeID: Int?, stopID: Int?, index: Int?) {
        db.update(
                "INSERT INTO route_stop (routeid, stopid, \"Index\") VALUES (?, ?, ?);",
                routeID, stopID, index
        )
    }

    override fun updateRoute(route: UpdateRoute): Int {
        db.update(
                "UPDATE route SET \"Name\" = ? WHERE \"ID\" = ?;",
                route.routeName, route.routeID
        )
        val routeID = db.query(
                "SELECT * FROM route WHERE serviceid = ? AND \"Name\" = ? AND isarchived = false",
                arrayOf(route.serviceID, route.routeName),{
            resultSet, rowNum -> RouteIDEntity(
                resultSet.getInt("ID")
        )
        }
        )
        return routeID[0].routeID
    }

    override fun deleteRouteStops(routeID: Int?) {
        db.update(
                "DELETE FROM route_stop WHERE routeid = ?;",
                routeID
        )
    }

    override fun updateAssignments(routeID: Int?) {
        db.update(
                "UPDATE assignment SET routeid = null, routename = 'Custom Route' WHERE routeid = ?;",
                routeID
        )
    }

    override fun archiveRoute(routeID: Int) {
        db.update(
                "UPDATE route SET isarchived = true WHERE \"ID\" = ?;",
                routeID
        )
    }

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
}