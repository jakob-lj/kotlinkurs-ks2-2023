package no.liflig.ks2kurs.common.http4k

import no.liflig.ks2kurs.common.auth.UserPrincipalLens
import no.liflig.ks2kurs.common.health.HealthService
import no.liflig.ks2kurs.common.health.health
import no.liflig.ks2kurs.common.http4k.errors.CatchExceptionsFilter
import no.liflig.logging.ErrorLog
import no.liflig.logging.NormalizedStatus
import no.liflig.logging.RequestResponseLog
import no.liflig.logging.http4k.CatchAllExceptionFilter
import no.liflig.logging.http4k.ErrorResponseRendererWithLogging
import no.liflig.logging.http4k.LoggingFilter
import no.liflig.logging.http4k.RequestIdMdcFilter
import no.liflig.logging.http4k.RequestLensFailureFilter
import org.http4k.contract.JsonErrorResponseRenderer
import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.format.Jackson
import org.http4k.lens.RequestContextKey
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import java.util.UUID

class ServiceRouter(
  logHandler: (RequestResponseLog<UserPrincipalLog>) -> Unit,
  principalToLog: (UserPrincipal) -> UserPrincipalLog,
  corsPolicy: CorsPolicy?,
  contexts: RequestContexts,
  userPrincipalLens: UserPrincipalLens,
  private val healthService: HealthService? = null,
) {

  private val requestIdChainLens = RequestContextKey.required<List<UUID>>(contexts)
  private val errorLogLens = RequestContextKey.optional<ErrorLog>(contexts)
  private val normalizedStatusLens = RequestContextKey.optional<NormalizedStatus>(contexts)

  val errorResponseRenderer = ErrorResponseRendererWithLogging(
    errorLogLens,
    normalizedStatusLens,
    JsonErrorResponseRenderer(Jackson),
  )

  /**
   * This code is unused, however, was used for returning errors by api handler
   */
  /*
    val errorToContext: (Request, Throwable) -> Unit = { request, throwable ->
      request.with(errorLogLens of ErrorLog(throwable))
    }
  */

  private val principalLog = { request: Request ->
    userPrincipalLens(request)?.let(principalToLog)
  }

  val coreFilters =
    ServerFilters
      .InitialiseRequestContext(contexts)
      .then(RequestIdMdcFilter(requestIdChainLens))
      .then(CatchAllExceptionFilter())
      .then(
        LoggingFilter(
          principalLog,
          errorLogLens,
          normalizedStatusLens,
          requestIdChainLens,
          logHandler,
        ),
      )
      .let {
        if (corsPolicy != null) it.then(ServerFilters.Cors(corsPolicy))
        else it
      }
      // .then(ErrorHandlerFilter(errorLogLens)) this seems to be FNF specific
      .then(RequestLensFailureFilter(errorResponseRenderer))
      .then(CatchExceptionsFilter())

  class RoutingBuilder(val errorResponseRenderer: ErrorResponseRendererWithLogging) {
    val additionalFilters = org.http4k.util.Appendable<Filter>()
    val routes = org.http4k.util.Appendable<RoutingHttpHandler>()
  }

  fun routingHandler(funk: RoutingBuilder.() -> Unit): RoutingHttpHandler {
    val builder = RoutingBuilder(errorResponseRenderer)
    builder.funk()

    var current = coreFilters
    builder.additionalFilters.all.forEach {
      current = current.then(it)
    }

    // add health routes
    val routes = builder.routes.all + listOfNotNull(healthService?.let { health(it) })

    // TODO: Hva skjer her?!
    return current.then(
      routes(*(routes).toTypedArray()),
    )
  }
}
