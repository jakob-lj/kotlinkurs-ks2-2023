package no.liflig.ks2kurs.services.person.domain

import no.liflig.ks2kurs.common.serialization.KotlinXSerializationAdapter

val personSerializationAdapter = KotlinXSerializationAdapter(Person.serializer())
