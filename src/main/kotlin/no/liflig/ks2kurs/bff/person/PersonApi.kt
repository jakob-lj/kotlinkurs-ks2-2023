package no.liflig.ks2kurs.bff.person

import no.liflig.ks2kurs.bff.person.routes.CreatePersonRoute
import no.liflig.ks2kurs.bff.person.routes.ListPersonRoute
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Api
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Method

class PersonApi(
  override val prefix: String,
  override val serviceRegistry: ServiceRegistry,
) : Api {

  private val listPersons = ListPersonRoute(serviceRegistry)
  private val createPerson = CreatePersonRoute(serviceRegistry)

  override val routes: List<ContractRoute> = listOf(
    prefix meta createPerson.meta()
      bindContract Method.POST to createPerson.handler(),

    prefix meta listPersons.meta()
      bindContract Method.GET to listPersons.handler(),
  )
}
