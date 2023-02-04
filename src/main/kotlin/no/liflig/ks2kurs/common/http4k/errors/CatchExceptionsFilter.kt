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
  if (error is AuthApiError) {
    return when (error.code) {
      AuthApiErrorCode.USER_PRINCIPAL_NOT_FOUND -> error.code.toResponse(Status.NOT_FOUND)
      AuthApiErrorCode.NOT_AUTHENTICATED -> error.code.toResponse(Status.UNAUTHORIZED)
      AuthApiErrorCode.USER_ALREADY_EXISTS -> error.code.toResponse(Status.BAD_REQUEST)
      AuthApiErrorCode.FORBIDDEN -> error.code.toResponse(Status.FORBIDDEN)
    }
  } else if (error is BoardApiError) {
    return when (error.code) {
      BoardApiErrorCode.COULD_NOT_FIND_BOARD -> error.code.toResponse(Status.NOT_FOUND)
    }
  } else throw IllegalStateException("Could not convert ApiError Response.")
}

fun ApiErrorCode.toResponse(status: Status) =
  Response(status).with(ApiFailureResponse.bodyLens of ApiFailureResponse(code = this.toString()))
