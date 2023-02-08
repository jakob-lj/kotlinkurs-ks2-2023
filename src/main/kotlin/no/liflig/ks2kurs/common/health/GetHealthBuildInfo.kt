@file:UseSerializers(InstantSerializer::class)

package no.liflig.ks2kurs.common.health

import kotlinx.serialization.UseSerializers
import no.liflig.ks2kurs.common.serialization.InstantSerializer
import java.time.Instant
import java.util.Properties

/**
 * Create [HealthBuildInfo] based on build.properties injected by the build.
 */
fun getHealthBuildInfo() = HealthBuildInfo(
  timestamp =
  Instant.now(),
)
