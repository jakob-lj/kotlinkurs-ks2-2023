package no.liflig.ks2kurs.common.http4k.errors

class PersonError(override val code: PersonErrorCode, override val throwable: Throwable?) : ApiError()

enum class PersonErrorCode : ApiErrorCode {
  PersonNotFound,
  InvalidBirthDay,
}
