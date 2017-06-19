package michalz.onzo.util

import michalz.onzo.model.SensorRead

import scala.io.Source

object DataStreamBuilder {

  def withDataStream[Result](resourceName: String)(code: Stream[SensorRead] => Result): Result = {
    val src = Source.fromInputStream(getClass.getResourceAsStream(resourceName))
    try {
      val stream = src.getLines().toStream.drop(1).map(SensorRead.fromCSVLine).collect {
        case Right(sensorRead) => sensorRead
      }

      code(stream)

    } finally {
      src.close()
    }
  }
}
