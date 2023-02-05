package no.liflig.ks2kurs.common.http4k.errors

import mu.KLogging
import no.liflig.ks2kurs.common.http4k.AuthApiError
import no.liflig.ks2kurs.common.http4k.AuthApiErrorCode
import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

@kotlinx.serialization.Serializable
class ApiFailureResponse(val code: String) {

  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
  }
}

@kotlinx.serialization.Serializable
class CustomApiFailureResponse(val feature: String, val customMessage: String) {
  val type = "CUSTOM"

  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
  }
}

object CatchExceptionsFilter : KLogging() {
  operator fun invoke() = Filter { next ->
    { req ->
      try {
        next(req)
      } catch (e: Throwable) {
        when (e) {
          is ApiError -> handleApiError(e)
          else -> {
            logger.error { e }
            Response(Status.INTERNAL_SERVER_ERROR).body("Something went terribly wrong.")
          }
        }
      }
    }
  }
}

fun handleApiError(error: ApiError): Response {
  return when (error) {
    is AuthApiError -> when (error.code) {
      AuthApiErrorCode.USER_PRINCIPAL_NOT_FOUND -> error.code.toResponse(Status.NOT_FOUND)
      AuthApiErrorCode.NOT_AUTHENTICATED -> error.code.toResponse(Status.UNAUTHORIZED)
      AuthApiErrorCode.USER_ALREADY_EXISTS -> error.code.toResponse(Status.BAD_REQUEST)
      AuthApiErrorCode.FORBIDDEN -> error.code.toResponse(Status.FORBIDDEN)
    }

    is PersonError -> when (error.code) {
      PersonErrorCode.PersonNotFound,
      PersonErrorCode.InvalidBirthDay,
      PersonErrorCode.CannotDeletePersonIfCarsHasPerson,
      -> error.code.toResponse(Status.BAD_REQUEST)
    }

    is CarError -> when (error.code) {
      CarErrorCode.CarNotFound,
      CarErrorCode.CarAlreadyExists,
      CarErrorCode.InvalidRegistrationNumber,
      CarErrorCode.NoAvailableSeats,
      CarErrorCode.PersonIsDriver,
      CarErrorCode.PersonIsPassenger,
      -> error.code.toResponse(Status.BAD_REQUEST)

      CarErrorCode.Custom -> Response(Status.BAD_REQUEST).with(
        CustomApiFailureResponse.bodyLens of
          CustomApiFailureResponse(
            feature = "car",
            customMessage = error.customMessage ?: "Custom message not implemented",
          ),
      )
    }

    else -> throw IllegalStateException("Could not convert ApiError Response.")
  }
}

fun ApiErrorCode.toResponse(status: Status) =
  Response(status).with(ApiFailureResponse.bodyLens of ApiFailureResponse(code = this.toString()))
