package no.liflig.ks2kurs.bff.person.routes

import kotlinx.coroutines.runBlocking
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Route
import no.liflig.ks2kurs.services.person.PersonServiceListFilter
import no.liflig.ks2kurs.services.person.dtos.PersonsDto
import no.liflig.ks2kurs.services.person.dtos.toDto
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.boolean
import org.http4k.lens.int
import java.time.Year

val licenseQuery = Query.boolean().optional("hasLicense")
val birthYearQuery = Query.int().optional("birthYear")

class ListPersonRoute(override val sr: ServiceRegistry) : Route {
  override fun meta(): RouteMetaDsl.() -> Unit = {
    summary = "list all persons"
    queries += licenseQuery
    queries += birthYearQuery
    returning(Status.OK, PersonsDto.bodyLens to PersonsDto.example)
  }

  override fun handler(vararg params: String): HttpHandler = { req ->

    val birthYear = birthYearQuery(req)
    val hasLicense = licenseQuery(req)

    val filter =
      PersonServiceListFilter(birthYear = birthYear?.let { Year.parse(birthYear.toString()) }, hasLicense = hasLicense)

    val persons = runBlocking { sr.personService.getByFilter(filter) }

    Response(Status.OK).with(PersonsDto.bodyLens of PersonsDto(items = persons.map { it.toDto() }))
  }
}
