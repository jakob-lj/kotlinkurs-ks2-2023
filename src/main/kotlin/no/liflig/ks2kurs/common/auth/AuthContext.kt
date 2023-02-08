package no.liflig.ks2kurs.common.auth

import no.liflig.ks2kurs.common.http4k.UserPrincipal
import org.http4k.lens.RequestContextLens

typealias UserPrincipalLens = RequestContextLens<UserPrincipal?>

val someAuthLens = "unused"
