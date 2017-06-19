package michalz.onzo.calculation

import michalz.onzo.model.SensorRead

trait Calculation {
  def update(element: SensorRead): Calculation
  def result: BigDecimal
}

case class TotalCalculation(precondition: Precondition, result: BigDecimal = 0) extends Calculation {

  override def update(element: SensorRead): Calculation =
    if(precondition.test(element)) copy(result = result + element.consumption)
    else this
}

case class AverageCalculation(precondition: Precondition, sum: BigDecimal = 0, numberOfElements: Long = 0) extends Calculation {

  override def update(element: SensorRead): Calculation =
    if(precondition.test(element)) copy(sum = sum + element.consumption, numberOfElements = numberOfElements + 1)
    else this

    override def result: BigDecimal =
      if(numberOfElements > 0) sum / numberOfElements
      else 0
}

