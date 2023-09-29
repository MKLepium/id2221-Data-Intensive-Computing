import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.streaming.api.scala._

case class BusData(
  dev: String,
  time: String,
  lat: String,
  lon: String,
  head: String,
  fix: String,
  route: String,
  stop: String,
  next: String,
  code: String,
  fer: String // Add the "fer" field
)

object ParseXMLFunction extends ProcessFunction[String, BusData] {

  lazy val busDataState: MapState[String, BusData] =
    getRuntimeContext.getMapState(new MapStateDescriptor[String, BusData]("busDataState", classOf[String], classOf[BusData]))

  override def processElement(
    xml: String,
    ctx: ProcessFunction[String, BusData]#Context,
    out: Collector[BusData]
  ): Unit = {
    // Parse the XML and extract relevant fields
    val busElement = scala.xml.XML.loadString(xml)
    val busAttributes = busElement.attributes
    val dev = busAttributes("dev").text // Extract the "dev" field
    val time = busAttributes("time").text
    val lat = busAttributes("lat").text
    val lon = busAttributes("lon").text
    val head = busAttributes("head").text
    val fix = busAttributes("fix").text
    val route = busAttributes("route").text
    val stop = busAttributes("stop").text
    val next = busAttributes("next").text
    val code = busAttributes("code").text
    val fer = busAttributes("fer").text // Extract the "fer" field

    // Create a BusData object
    val busData = BusData(dev, time, lat, lon, head, fix, route, stop, next, code, fer)

    // Store the BusData object in the state
    busDataState.put(time, busData)

    // Emit the BusData object
    out.collect(busData)
  }
}
