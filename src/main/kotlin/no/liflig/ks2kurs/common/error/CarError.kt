package no.liflig.ks2kurs.common.error

class CarError(override val code: CarErrorCode, override val throwable: Throwable?) : ApiError()

enum class CarErrorCode : ApiErrorCode {
  CarNotFound,
  CarAlreadyExists,
  InvalidRegistrationNumber,
}
