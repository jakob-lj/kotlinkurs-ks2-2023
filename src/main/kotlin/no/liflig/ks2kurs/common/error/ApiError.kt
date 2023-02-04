package no.liflig.ks2kurs.common.error

import kotlinx.serialization.Serializable

@Serializable
abstract class ApiError() : RuntimeException() {
  abstract val code: ApiErrorCode
  abstract val throwable: Throwable?
}

interface ApiErrorCode
