package michalz.onzo.calculation

import java.time.{LocalDateTime, ZoneOffset}

import michalz.onzo.model.SensorRead
import org.scalatest.{FlatSpec, Matchers}

class PreconditionSpec extends FlatSpec with Matchers {

  val zero = BigDecimal(0)
  val baseDate = LocalDateTime.parse("2017-03-17T00:00").atZone(ZoneOffset.UTC)
  val sensorId = "s1"

  "SensorIdPrecondition" should "pass matched element" in {
    SensorIdPrecondition(sensorId).test(SensorRead(sensorId, baseDate, zero)) should be (true)
  }

  it should "discard not matched element" in {
    SensorIdPrecondition(sensorId).test(SensorRead(sensorId + "invalid", baseDate, zero)) should be (false)
  }


  "InclusiveTimeRangePrecondition" should "pass elements in range" in {

    val rightEdgeElemnt = SensorRead(sensorId, baseDate.plusHours(4), zero)
    val leftEdgeElement = SensorRead(sensorId, baseDate, zero)
    val middleRangeElement = SensorRead(sensorId, baseDate, zero)

    val precondition = InclusiveTimeRangePrecondition(baseDate, baseDate.plusHours(4))
    precondition.test(rightEdgeElemnt) should be (true)
    precondition.test(leftEdgeElement) should be (true)
    precondition.test(middleRangeElement) should be (true)
  }

  it should "discard element outside range" in {
    val beforeRangeElement = SensorRead(sensorId, baseDate.minusHours(1), zero)
    val afterRangeElement = SensorRead(sensorId, baseDate.plusHours(5), zero)

    val precondition = InclusiveTimeRangePrecondition(baseDate, baseDate.plusHours(4))
    precondition.test(beforeRangeElement) should be (false)
    precondition.test(afterRangeElement) should be (false)
  }

  "ListPrecondition" should "pass elements that matches all preconditions" in {
    val precondition = ListPrecondition(List(
      SensorIdPrecondition(sensorId),
      InclusiveTimeRangePrecondition(baseDate, baseDate.plusHours(4))
    ))

    precondition.test(SensorRead(sensorId, baseDate.plusHours(2), zero)) should be (true)
  }

  it should "discard elements that don't match all preconditions" in {
    val precondition = ListPrecondition(List(
      SensorIdPrecondition(sensorId),
      InclusiveTimeRangePrecondition(baseDate, baseDate.plusHours(4))
    ))

    val invalidIdElement = SensorRead(sensorId + "invalid", baseDate.plusHours(2), zero)
    val outsideRangeElement = SensorRead(sensorId, baseDate.plusHours(5), zero)
    val allInvalidElement = SensorRead(sensorId + "invalid", baseDate.plusHours(5), zero)

    precondition.test(invalidIdElement) should be (false)
    precondition.test(outsideRangeElement) should be (false)
    precondition.test(allInvalidElement) should be (false)
  }
}
