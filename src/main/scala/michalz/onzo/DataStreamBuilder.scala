package michalz.onzo

import scala.io.Source

object DataStreamBuilder {

  def fromResourceCSVFile(resourceName: String): Stream[String] = {
    val src = Source.fromInputStream(getClass.getResourceAsStream(resourceName))
    try {
      src.getLines().toStream.drop(1)
    } finally {
      src.close()
    }
  }
}
