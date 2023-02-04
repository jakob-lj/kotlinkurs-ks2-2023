package no.liflig.ks2kurs.common.http4k

import org.http4k.contract.ContractRoute

interface Api {
  val prefix: String
  val routes: List<ContractRoute>
}
