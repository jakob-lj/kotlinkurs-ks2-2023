package no.liflig.ks2kurs.bff.person.routes

import kotlinx.coroutines.runBlocking
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Route
import no.liflig.ks2kurs.services.person.domain.PersonId
import no.liflig.ks2kurs.services.person.dtos.CreateOrEditPersonRequest
import no.liflig.ks2kurs.services.person.dtos.PersonDto
import no.liflig.ks2kurs.services.person.dtos.toDto
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

class EditPersonRoute(override val sr: ServiceRegistry) : Route {
  override fun meta(): RouteMetaDsl.() -> Unit = {
    summary = "edit person"
    receiving(CreateOrEditPersonRequest.bodyLens to CreateOrEditPersonRequest.example)
    returning(Status.OK, PersonDto.bodyLens to PersonDto.example)
  }

  override fun handler(vararg params: String): HttpHandler = { req ->

    val personId = PersonId(params.first())

    val request = CreateOrEditPersonRequest.bodyLens(req)

    val updatedPerson = runBlocking { sr.personService.edit(request, personId) }

    Response(Status.OK).with(PersonDto.bodyLens of updatedPerson.toDto())
  }
}
