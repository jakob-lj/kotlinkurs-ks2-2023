package no.liflig.ks2kurs.common.http4k

import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler

interface Route {
  fun meta(): RouteMetaDsl.() -> Unit

  fun handler(): HttpHandler
}
