package no.liflig.ks2kurs.common.auth

sealed class GetPrincipalDeviation {
  object PrincipalNotFound : GetPrincipalDeviation()
}
