package no.liflig.ks2kurs.bff.person

import no.liflig.ks2kurs.bff.person.routes.CreatePersonRoute
import no.liflig.ks2kurs.bff.person.routes.EditPersonRoute
import no.liflig.ks2kurs.bff.person.routes.ListPersonRoute
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Api
import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Method
import org.http4k.lens.Path

class PersonApi(
  override val prefix: String,
  override val serviceRegistry: ServiceRegistry,
) : Api {

  private val listPersons = ListPersonRoute(serviceRegistry)
  private val createPerson = CreatePersonRoute(serviceRegistry)
  private val editPerson = EditPersonRoute(serviceRegistry)

  override val routes: List<ContractRoute> = listOf(
    prefix meta createPerson.meta()
      bindContract Method.POST to createPerson.handler(),

    prefix meta listPersons.meta()
      bindContract Method.GET to listPersons.handler(),

    prefix / Path.of("personId") meta editPerson.meta()
      bindContract Method.PUT to { personId -> editPerson.handler(personId) },
  )
}
