package no.liflig.ks2kurs.common.auth

import arrow.core.Either
import arrow.core.right
import mu.KLogging
import no.liflig.ks2kurs.common.http4k.UserPrincipal
import org.http4k.core.Request

object DummyAuthService : AuthService, KLogging() {

  override suspend fun getPrincipal(request: Request): Either<GetPrincipalDeviation, UserPrincipal?> {
    logger.info { "Returning dummy principal" }
    return UserPrincipal(
      user = User(
        id = UserId(),
        userName = "some name",
      ),
    ).right()
  }
}
