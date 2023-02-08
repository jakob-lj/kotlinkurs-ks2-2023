package no.liflig.ks2kurs.common.http4k

import kotlinx.serialization.json.Json
import mu.KotlinLogging
import net.logstash.logback.marker.Markers
import org.http4k.core.Filter
import org.http4k.core.Headers
import org.http4k.core.Response
import org.http4k.core.Status
import java.time.Instant

val logger = KotlinLogging.logger {}

val json = Json { }

internal fun deriveNormalizedStatus(response: Response): NormalizedStatus {
  val s = response.status
  return when {
    s.successful || s.informational || s.redirection -> NormalizedStatus.Ok()
    s.clientError -> when (s) {
      Status.UNAUTHORIZED -> NormalizedStatus.ClientError(ClientErrorCategory.UNAUTHORIZED)
      Status.NOT_FOUND -> NormalizedStatus.ClientError(ClientErrorCategory.NOT_FOUND)
      else -> NormalizedStatus.ClientError(ClientErrorCategory.BAD_REQUEST)
    }

    s == Status.SERVICE_UNAVAILABLE -> NormalizedStatus.ServiceUnavailable()
    else -> NormalizedStatus.InternalServerError()
  }
}

private fun cleanAndNormalizeHeaders(headers: Headers, redactedHeaders: List<String>): List<Map<String, String?>> =
  headers.map { (name, value) ->
    mapOf(
      "name" to name,
      "value" to when {
        redactedHeaders.any { it.equals(name, ignoreCase = true) } -> "*REDACTED*"
        else -> value
      },
    )
  }

object LoggingFilter {
  operator fun invoke(
    logHandler: (RequestResponseLog) -> Unit,
    includeBody: Boolean = true,
    redactedHeaders: List<String> = listOf("authorization", "x-api-key"),
  ) = Filter { next ->
    { request ->

      val response = next(request)

      val logEntry = RequestResponseLog(
        timestamp = Instant.now(),
        request = RequestLog(
          method = request.method.toString(),
          uri = request.uri.toString(),
          headers = cleanAndNormalizeHeaders(request.headers, listOf()),
          size = null,
          timestamp = Instant.now(),
          body = null,
        ),
        response = ResponseLog(
          timestamp = Instant.now(),
          statusCode = response.status.code,
          headers = cleanAndNormalizeHeaders(response.headers, redactedHeaders),
          size = response.body.length?.toInt(),
          body = null,
        ),
        throwable = null,
        status = deriveNormalizedStatus(response),
        durationMs = 69,
        thread = Thread.currentThread().name,
      )

      logHandler(logEntry)

      response
    }
  }

  fun createLogHandler(
    printStacktraceToConsole: Boolean,
    suppressSuccessfulHealthChecks: Boolean = true,
  ): (RequestResponseLog) -> Unit {
    return handler@{ entry ->
      val request = entry.request
      val response = entry.response

      if (suppressSuccessfulHealthChecks && request.uri == "/health" && response.statusCode == 200 && entry.throwable == null) {
        return@handler
      }

      logger.info(
        "HTTP request (${response.statusCode}) (${entry.durationMs} ms): ${request.method} ${request.uri}",
        Markers.appendRaw(
          "requestInfo",
          json.encodeToString(RequestResponseLog.serializer(), entry),
        ),
      )

      val throwable = entry.throwable
      if (printStacktraceToConsole && throwable != null) {
        // Using println instead of logger to not scramble logfile.
        println("Throwable from request ${request.method} ${request.uri}:")
        throwable.printStackTrace()
      }
    }
  }
}
