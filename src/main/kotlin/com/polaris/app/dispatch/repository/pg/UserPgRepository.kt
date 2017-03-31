package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.UserRepository
import com.polaris.app.dispatch.repository.UserType
import com.polaris.app.dispatch.repository.entity.UserEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class UserPgRepository(val db: JdbcTemplate) : UserRepository {
    override fun findUserByLogin(username: String, password: String, servicecode: String): List<UserEntity> {
        var sql = "SELECT u.\"ID\", u.serviceid, u.fname, u.lname, u.username, u.usertype "
        sql += "FROM \"user\" AS u "
        sql += "INNER JOIN service AS s ON (s.serviceid = u.serviceid) "
        sql += "WHERE u.username = ? AND u.\"Password\" = ? AND s.servicecode = ?"

        val UserEntities = db.query(
                sql,
                arrayOf(username, password, servicecode),
                {
                    resultSet, rowNum -> UserEntity(
                        id = resultSet.getInt("ID"),
                        serviceId = resultSet.getInt("serviceid"),
                        firstName = resultSet.getString("fname"),
                        lastName = resultSet.getString("lname"),
                        userName = resultSet.getString("username"),
                        userType = UserType.valueOf(resultSet.getString("usertype"))
                    )
                }
        )
        return UserEntities
    }
}