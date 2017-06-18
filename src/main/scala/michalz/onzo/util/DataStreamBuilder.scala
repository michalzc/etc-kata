package michalz.onzo.util

import michalz.onzo.model.SensorRead

import scala.io.Source

object DataStreamBuilder {
//
//  def fromResourceCSVFile(resourceName: String): Stream[SensorRead] = {
//    val src = Source.fromInputStream(getClass.getResourceAsStream(resourceName))
//    try {
//      src.getLines().toStream.drop(1).map(SensorRead.fromCSVLine).collect {
//        case Right(sensorRead) => sensorRead
//      }
//    } finally {
//      src.close()
//    }
//  }

  def withDataStream(resourceName: String)(code: Stream[SensorRead] => Any): Any = {
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
