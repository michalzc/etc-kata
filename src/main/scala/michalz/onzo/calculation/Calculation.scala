package michalz.onzo.calculation

import michalz.onzo.model.SensorRead

trait Calculation {
  def calculate(dataStream: Stream[SensorRead]): BigDecimal
}

case class TotalCalculation(precondition: Precondition) extends Calculation {
  override def calculate(dataStream: Stream[SensorRead]): BigDecimal = dataStream.filter(precondition.test).map(_.consumption).sum
}

case class AverageCalculation(precondition: Precondition) extends Calculation {
  override def calculate(dataStream: Stream[SensorRead]): BigDecimal = {
    val (t, q) = dataStream.filter(precondition.test).foldLeft((0, BigDecimal(0))) {
      (total, item) => (total._1 + 1, total._2 + item.consumption)
    }

    if(t == 0) 0 else q / t
  }
}

