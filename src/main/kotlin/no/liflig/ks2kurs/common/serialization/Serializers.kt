package no.liflig.ks2kurs.common.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import no.liflig.ks2kurs.common.db.EntityId
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object InstantSerializer : KSerializer<Instant> {
  private val formatter = DateTimeFormatter.ISO_INSTANT

  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("InstantSerializer", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Instant): Unit =
    encoder.encodeString(formatter.format(value))

  override fun deserialize(decoder: Decoder): Instant =
    formatter.parse(decoder.decodeString(), Instant::from)
}

object BigDecimalSerializer : KSerializer<BigDecimal> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: BigDecimal) =
    encoder.encodeString(value.toString())

  override fun deserialize(decoder: Decoder): BigDecimal = BigDecimal(decoder.decodeString())
}

object LocalDateSerializer : KSerializer<LocalDate> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("LocalDateSerializer", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: LocalDate): Unit =
    encoder.encodeString(value.toString())

  override fun deserialize(decoder: Decoder): LocalDate =
    LocalDate.parse(decoder.decodeString())
}

abstract class UuidEntityIdSerializer<T : EntityId>(
  val factory: (UUID) -> T,
) : KSerializer<T> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("UuidEntityId", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: T) =
    encoder.encodeString(value.toString())

  override fun deserialize(decoder: Decoder): T =
    factory(UUID.fromString(decoder.decodeString()))
}
