package michalz.onzo.model

import java.time.{LocalDateTime, ZoneOffset}

import org.scalatest.{EitherValues, FlatSpec, Matchers}

class SensorReadSpec extends FlatSpec with Matchers with EitherValues {

  behavior of "SensorRead"

  val validSensorReadLine = "b08c6195-8cd9-43ab-b94d-e0b887dd73d2,2017-03-30T02:00:00Z,92"
  val invalidTimestampLine = "b08c6195-8cd9-43ab-b94d-e0b887dd73d2,invalidTimestamp,92"
  val invalidConsumptionLine = "b08c6195-8cd9-43ab-b94d-e0b887dd73d2,2017-03-30T02:00:00Z,invalidConsumption"
  val missingFiledsLine = "b08c6195-8cd9-43ab-b94d-e0b887dd73d2,92"
  val tooMuchFieldsLine = "b08c6195-8cd9-43ab-b94d-e0b887dd73d2,2017-03-30T02:00:00Z,92,55"
  val emptyLine = ""

  it should "deserialize valid line" in {
    SensorRead.fromCSVLine(validSensorReadLine).right.value shouldBe
      SensorRead("b08c6195-8cd9-43ab-b94d-e0b887dd73d2", LocalDateTime.of(2017, 3, 30, 2, 0).atZone(ZoneOffset.UTC), BigDecimal(92))
  }

  it should "failed on line with invalid timestamp" in {
    SensorRead.fromCSVLine(invalidTimestampLine).left.value shouldBe
      "Invalid timestamp"
  }

  it should "failed on line with invalid consumption" in {
    SensorRead.fromCSVLine(invalidConsumptionLine).left.value shouldBe
      "Invalid consumption"
  }

  it should "failed on line with invalid number of fields" in {
    val msg = "Invalid number of fields"

    SensorRead.fromCSVLine(missingFiledsLine).left.value shouldBe msg
    SensorRead.fromCSVLine(tooMuchFieldsLine).left.value shouldBe msg
    SensorRead.fromCSVLine(emptyLine).left.value shouldBe msg
  }
}
