package no.liflig.ks2kurs.common.auth

import java.util.UUID

data class User(val id: UserId, val userName: String)

data class UserId(val id: UUID = UUID.randomUUID())
