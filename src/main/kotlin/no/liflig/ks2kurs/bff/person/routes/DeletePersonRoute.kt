package no.liflig.ks2kurs.bff.person.routes

import kotlinx.coroutines.runBlocking
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Route
import no.liflig.ks2kurs.common.http4k.errors.PersonError
import no.liflig.ks2kurs.common.http4k.errors.PersonErrorCode
import no.liflig.ks2kurs.services.person.domain.PersonId
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status

class DeletePersonRoute(override val sr: ServiceRegistry) : Route {
  override fun meta(): RouteMetaDsl.() -> Unit = {
    summary = "delete person"
    returning(Status.OK)
  }

  override fun handler(vararg params: String): HttpHandler = {
    val personId = PersonId(params.first())

    runBlocking {
      val person = sr.personRepository.get(personId) ?: throw PersonError(PersonErrorCode.PersonNotFound)

      sr.carService.getCarsWithPerson(personId).forEach {
        sr.carService.removeFromCar(personId, it.id)
      }

      if (sr.carService.getCarsWithPerson(personId).isNotEmpty()) {
        throw PersonError(PersonErrorCode.CannotDeletePersonIfCarsHasPerson)
      }

      sr.personRepository.delete(person)
    }

    Response(Status.OK)
  }
}
