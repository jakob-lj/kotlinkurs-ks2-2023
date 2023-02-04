package no.liflig.ks2kurs.common.http4k

import no.liflig.ks2kurs.common.domain.ServiceRegistry
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler

interface Route {

  val sr: ServiceRegistry

  fun meta(): RouteMetaDsl.() -> Unit

  fun handler(): HttpHandler
}
