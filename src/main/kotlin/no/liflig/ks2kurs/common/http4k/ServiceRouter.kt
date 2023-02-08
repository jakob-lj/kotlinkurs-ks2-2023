package no.liflig.ks2kurs.common.http4k

import no.liflig.ks2kurs.common.auth.UserPrincipalLens
import no.liflig.ks2kurs.common.health.HealthService
import no.liflig.ks2kurs.common.health.health
import no.liflig.ks2kurs.common.http4k.errors.CatchExceptionsFilter
import org.http4k.core.Filter
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.events.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.routes
import java.util.UUID

class ServiceRouter(
  corsPolicy: CorsPolicy?,
  contexts: RequestContexts,
  userPrincipalLens: UserPrincipalLens,
  private val healthService: HealthService? = null,
) {

  private val requestIdChainLens = RequestContextKey.required<List<UUID>>(contexts)

  /**
   * This code is unused, however, was used for returning errors by api handler
   */
  /*
    val errorToContext: (Request, Throwable) -> Unit = { request, throwable ->
      request.with(errorLogLens of ErrorLog(throwable))
    }
  */

  val coreFilters =
    ServerFilters
      .InitialiseRequestContext(contexts)
      .then(
        LoggingFilter.invoke(
          LoggingFilter.createLogHandler(
            printStacktraceToConsole = true,
          ),
        ),
      )
      .let {
        if (corsPolicy != null) it.then(ServerFilters.Cors(corsPolicy))
        else it
      }
      // .then(ErrorHandlerFilter(errorLogLens)) this seems to be FNF specific
      .then(CatchExceptionsFilter())

  class RoutingBuilder() {
    val additionalFilters = org.http4k.util.Appendable<Filter>()
    val routes = org.http4k.util.Appendable<RoutingHttpHandler>()
  }

  fun routingHandler(funk: RoutingBuilder.() -> Unit): RoutingHttpHandler {
    val builder = RoutingBuilder()
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
