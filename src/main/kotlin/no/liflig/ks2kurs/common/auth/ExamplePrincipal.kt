package no.liflig.ks2kurs.common.auth

@JvmInline
value class Email(val value: String) {
  init {
    require(value.contains('@'))
  }
}

enum class PrincipalRole(val keycloakName: String) {
  ADMIN("admin"),
  NORMAL("vanlig"),
}
