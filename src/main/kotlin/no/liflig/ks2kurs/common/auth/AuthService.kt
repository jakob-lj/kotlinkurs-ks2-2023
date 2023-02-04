package no.liflig.ks2kurs.common.auth

import arrow.core.Either
import no.liflig.ks2kurs.common.http4k.UserPrincipal
import org.http4k.core.Request

interface AuthService {
  suspend fun getPrincipal(request: Request): Either<GetPrincipalDeviation, UserPrincipal?>
}
