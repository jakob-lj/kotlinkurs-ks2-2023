package no.liflig.ks2kurs.common.utils

import no.liflig.ks2kurs.common.db.DatabaseConfigurator
import org.jdbi.v3.core.Jdbi
import org.testcontainers.containers.PostgreSQLContainer

class KPostgreSQLContainer(imageName: String) : PostgreSQLContainer<KPostgreSQLContainer>(imageName)

fun createJdbiForTests(useStaticPort: Boolean): Jdbi {
  val username = "username"
  val password = "password"
  val imageName = "postgres:13.2"
  val pgContainer = KPostgreSQLContainer(imageName)

  pgContainer
    .withDatabaseName("app")
    .withUsername(username)
    .withPassword(password)

  pgContainer.start()

  // val url = pgContainer.jdbcUrl

  // Unsure if we need other db driver in the Hikari config during tests
  return DatabaseConfigurator.createJdbiInstanceAndMigrate(
    DatabaseConfigurator.createDataSource(
      pgContainer.jdbcUrl,
      username,
      password,
    ),
  )
}
