package no.liflig.ks2kurs.common.http4k.errors

import kotlinx.serialization.Serializable

@Serializable
abstract class ApiError() : RuntimeException() {
  abstract val code: ApiErrorCode
  abstract val throwable: Throwable?
}

interface ApiErrorCode

class BoardApiError(override val code: BoardApiErrorCode, override val throwable: Throwable? = null) : ApiError()

enum class BoardApiErrorCode : ApiErrorCode {
  COULD_NOT_FIND_BOARD
}
