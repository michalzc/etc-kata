package michalz.onzo.model

import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

import scala.util.control.Exception.catching

case class SensorRead(sensorId: String, timestamp: ZonedDateTime, consumption: BigDecimal)


object SensorRead {
  def fromCSVLine(line: String): Either[String, SensorRead] = {
    line.trim.split(',') match {
      case Array(sensorId, timestampString, consumptionString) => for {
        timestamp <- parseTimestamp(timestampString)
        consumption <- parseConsumption(consumptionString)
      } yield SensorRead(sensorId, timestamp, consumption)

      case _ => Left("Invalid number of fields")
    }
  }

  private def parseTimestamp(timestampString: String): Either[String, ZonedDateTime] =
    (catching(classOf[DateTimeParseException]) either ZonedDateTime.parse(timestampString)).left.map(_ => "Invalid timestamp")

  private def parseConsumption(consumptionString: String): Either[String, BigDecimal] =
    (catching(classOf[NumberFormatException]) either BigDecimal(consumptionString)).left.map(_ => "Invalid consumption")
}