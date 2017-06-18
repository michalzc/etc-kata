package michalz.onzo

import java.time.{LocalDateTime, ZoneOffset}

import michalz.onzo.calculation._
import michalz.onzo.util.DataStreamBuilder

object Runner extends App {

  val sensorIdPrecondition = SensorIdPrecondition("b08c6195-8cd9-43ab-b94d-e0b887dd73d2")

  val wholeDayCalculation = TotalCalculation(sensorIdPrecondition)

  val day = LocalDateTime.parse("2017-03-30T00:00").atZone(ZoneOffset.UTC)

  val zeroToSevenCalculation = AverageCalculation(ListPrecondition(List(
    sensorIdPrecondition,
    InclusiveTimeRangePrecondition(day, day.plusHours(7))
  )))

  val eightToThreeCalculation = AverageCalculation(ListPrecondition(List(
    sensorIdPrecondition,
    InclusiveTimeRangePrecondition(day.plusHours(8), day.plusHours(15))
  )))

  val fourToElevenCalculation = AverageCalculation(ListPrecondition(List(
    sensorIdPrecondition,
    InclusiveTimeRangePrecondition(day.plusHours(16), day.plusHours(23))
  )))

  DataStreamBuilder.withDataStream("/consumption_data.csv") { dataStream =>
    println(s"Total daily consumption: ${wholeDayCalculation.calculate(dataStream) / 1000}")
    println(s"from 00:00 to 07:00 average consumption: ${zeroToSevenCalculation.calculate(dataStream) / 1000}")
    println(s"from 08:00 to 15:00 average consumption: ${eightToThreeCalculation.calculate(dataStream) / 1000}")
    println(s"from 16:00 to 23:00 average consumption: ${fourToElevenCalculation.calculate(dataStream) / 1000}")
  }
}
