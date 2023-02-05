package no.liflig.ks2kurs.bff

import no.liflig.ks2kurs.common.utils.createTestApp
import no.liflig.ks2kurs.services.person.dtos.CreateOrEditPersonRequest
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PersonTest {
  @Test
  fun PersonTest() {
    val httpHandler = createTestApp()

    val person = createPerson(
      httpHandler = httpHandler,
      createRequest = CreateOrEditPersonRequest(
        birthDay = LocalDate.parse("1970-01-01"),
        hasLicense = true,
        name = "Frank Franksen",
      ),
    )

    Assertions.assertEquals(Status.OK, person.status)

    val persons = listPersons(
      httpHandler = httpHandler,
    )

    Assertions.assertEquals(1, persons.items.size)
    Assertions.assertEquals("Frank Franksen", persons.items[0].name)

    val editedPerson = editPerson(
      httpHandler = httpHandler,
      personId = persons.items[0].id,
      createRequest = CreateOrEditPersonRequest(
        hasLicense = false,
        birthDay = LocalDate.parse("1970-02-01"),
        name = "Even Franksen",
      ),
    )

    Assertions.assertEquals(Status.OK, editedPerson.status)

    val updatedListOfPersons = listPersons(
      httpHandler = httpHandler,
    )

    Assertions.assertEquals(1, updatedListOfPersons.items.size)
    Assertions.assertEquals("Even Franksen", updatedListOfPersons.items[0].name)
    Assertions.assertEquals(false, updatedListOfPersons.items[0].hasLicense)
    Assertions.assertEquals(LocalDate.parse("1970-02-01"), updatedListOfPersons.items[0].birthDay)
  }
}
