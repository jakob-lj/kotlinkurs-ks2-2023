package no.liflig.ks2kurs.common.http4k

import kotlinx.serialization.Serializable
import no.liflig.logging.PrincipalLog

@Serializable
data class UserPrincipalLog(
  val userId: String,
) : PrincipalLog

fun UserPrincipal.toLog(): UserPrincipalLog = UserPrincipalLog(
  userId = this.user.id.id.toString(),
)
