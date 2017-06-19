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
    val calc = dataStream.foldLeft[Calculation](TotalCalculation(allMatchPrecondition))((c, e) => c.update(e))
    calc.result should be (BigDecimal(6))
  }

  it should "return 0 if all elements are discarded" in {
    val calc = dataStream.foldLeft[Calculation](TotalCalculation(discardAllPrecondition))((c, e) => c.update(e))
    calc.result should be (BigDecimal(0))
  }

  "AverageCalculation" should "calculate average from all emenets" in {
    val calc = dataStream.foldLeft[Calculation](AverageCalculation(allMatchPrecondition))((c, e) => c.update(e))
    calc.result should be (BigDecimal(2))
  }

  it should "return 0 if all elements are discarded" in {
    val calc = dataStream.foldLeft[Calculation](AverageCalculation(discardAllPrecondition))((c, e) => c.update(e))
    calc.result should be (BigDecimal(0))
  }


  private def mkElem(consumption: Int): SensorRead = SensorRead("sensorId", ZonedDateTime.now(), BigDecimal(consumption))
}
