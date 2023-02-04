package no.liflig.ks2kurs.api

import no.liflig.ks2kurs.common.utils.createTestApp
import org.http4k.core.Method
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HealthApiTest {

  @Test
  internal fun `health should respond 200 ok`() {
    // Given

    val router = createTestApp()

    // When
    val actual = router(org.http4k.core.Request(Method.GET, "/health"))

    // Then
    Assertions.assertEquals(Status.OK, actual.status)
  }
}
