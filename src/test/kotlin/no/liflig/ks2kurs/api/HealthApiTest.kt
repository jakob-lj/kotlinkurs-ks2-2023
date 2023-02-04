package no.liflig.ks2kurs.api

import no.liflig.ks2kurs.createApp
import no.liflig.ks2kurs.common.auth.DummyAuthService
import no.liflig.ks2kurs.common.config.Config
import no.liflig.http4k.health.HealthBuildInfo
import no.liflig.http4k.health.HealthService
import org.http4k.core.Method
import org.http4k.core.Status
import org.http4k.kotest.shouldHaveStatus
import org.junit.jupiter.api.Test
import java.time.Instant

class HealthApiTest {

  @Test
  internal fun `health should respond 200 ok`() {
    // Given
    val router = createApp(
      logHandler = { },
      policy = Config.load(),
      authService = DummyAuthService,
      healthService = HealthService(
        name = "Test-app",
        buildInfo = HealthBuildInfo(
          timestamp = Instant.now(),
          commit = "Initial commit",
          branch = "test",
          number = 123,
        ),
      ),
    )

    // When
    val actual = router(org.http4k.core.Request(Method.GET, "/health"))

    // Then
    actual shouldHaveStatus Status.OK
  }
}
