package no.liflig.ks2kurs.common.serialization

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class BigDecimalSerializerTest {
  @Test
  internal fun `should serialize to string`() {
    // Given
    val number = BigDecimal("16777217")

    // When
    val actual = Json.encodeToString(BigDecimalSerializer, number)

    // Then
    Assertions.assertEquals(actual, "\"16777217\"")
  }
}
