package no.liflig.ks2kurs.common.health

import kotlinx.serialization.json.Json
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.string
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind

private val jsonLens = Body.string(ContentType.APPLICATION_JSON).toLens()

fun health(healthService: HealthService): RoutingHttpHandler = "/health" bind Method.GET to {
  val healthStatusJson = Json.encodeToString(
    HealthStatus.serializer(),
    healthService.healthStatus(),
  )
  Response(Status.OK).with(jsonLens of healthStatusJson)
}
