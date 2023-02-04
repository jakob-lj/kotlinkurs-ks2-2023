package no.liflig.ks2kurs.common.http4k

import no.liflig.ks2kurs.common.http4k.errors.ApiError
import no.liflig.ks2kurs.common.http4k.errors.ApiErrorCode

class AuthApiError(override val code: AuthApiErrorCode, override val throwable: Throwable? = null) : ApiError()

enum class AuthApiErrorCode : ApiErrorCode {
  USER_PRINCIPAL_NOT_FOUND,
  NOT_AUTHENTICATED,
  USER_ALREADY_EXISTS,
  FORBIDDEN
}
