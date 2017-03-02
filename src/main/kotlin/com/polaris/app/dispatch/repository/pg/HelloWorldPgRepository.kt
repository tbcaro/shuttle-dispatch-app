package com.polaris.app.dispatch.repository.pg

import com.polaris.app.dispatch.repository.HelloWorldRepository
import com.polaris.app.dispatch.repository.entity.HelloWorldEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class HelloWorldPgRepository(val db: JdbcTemplate) : HelloWorldRepository {
    override fun findAll(): List<HelloWorldEntity> {
        val helloWorldEntities = db.query(
                "SELECT * FROM \"HelloWorld\"",
                {
                    resultSet, rowNum -> HelloWorldEntity(
                        resultSet.getString("HelloWorld")
                )
                }
        )

        return helloWorldEntities
    }
}