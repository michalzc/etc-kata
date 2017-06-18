package michalz.onzo.calculation

import java.time.ZonedDateTime

import michalz.onzo.model.SensorRead
import org.scalatest.{FlatSpec, Matchers}

class CalculationSpec extends FlatSpec with Matchers {

  val dataStream = mkElem(1) #:: mkElem(2) #:: mkElem(3) #:: Stream.empty[SensorRead]

  val allMatchPrecondition = new Precondition {
    override def test(sensorRead: SensorRead): Boolean = true
  }

  val discardAllPrecondition = new Precondition {
    override def test(sensorRead: SensorRead): Boolean = false
  }

  "TotalCalculation" should "calculate total from all elements" in {
    TotalCalculation(allMatchPrecondition).calculate(dataStream) should be (BigDecimal(6))
  }

  it should "return 0 if all elements are discarded" in {
    TotalCalculation(discardAllPrecondition).calculate(dataStream) should be (BigDecimal(0))
  }

  "AverageCalculation" should "calculate average from all emenets" in {
    AverageCalculation(allMatchPrecondition).calculate(dataStream) should be (BigDecimal(2))
  }

  it should "return 0 if all elements are discarded" in {
    AverageCalculation(discardAllPrecondition).calculate(dataStream) should be (BigDecimal(0))
  }


  private def mkElem(consumption: Int): SensorRead = SensorRead("sensorId", ZonedDateTime.now(), BigDecimal(consumption))
}
