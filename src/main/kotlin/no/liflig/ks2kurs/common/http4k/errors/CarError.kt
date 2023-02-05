package no.liflig.ks2kurs.common.http4k.errors

class CarError(
  override val code: CarErrorCode,
  override val throwable: Throwable? = null,
  val customMessage: String? = null,
) : ApiError()

enum class CarErrorCode : ApiErrorCode {
  CarNotFound,
  CarAlreadyExists,
  InvalidRegistrationNumber,
  NoAvailableSeats,
  Custom,
}
