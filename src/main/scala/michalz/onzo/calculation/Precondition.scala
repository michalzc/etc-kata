package michalz.onzo.calculation

import java.time.ZonedDateTime

import michalz.onzo.model.SensorRead

trait Precondition {
  def test(sensorRead: SensorRead): Boolean
}

case class SensorIdPrecondition(sensorId: String) extends Precondition {
  override def test(sensorRead: SensorRead): Boolean = sensorId == sensorRead.sensorId
}

case class InclusiveTimeRangePrecondition(from: ZonedDateTime, to: ZonedDateTime) extends Precondition {
  override def test(sensorRead: SensorRead): Boolean =
    (from.isEqual(sensorRead.timestamp) || from.isBefore(sensorRead.timestamp)) &&
      (to.isEqual(sensorRead.timestamp) || to.isAfter(sensorRead.timestamp))
}

case class ListPrecondition(preconditions: List[Precondition]) extends Precondition {
  override def test(sensorRead: SensorRead): Boolean = preconditions.map(_.test(sensorRead)).fold(true)(_ && _)
}