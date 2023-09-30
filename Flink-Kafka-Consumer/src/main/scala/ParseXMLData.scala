package com.Flink_Kafka_Consumer.XML_Parser

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.api.scala._
import com.Flink_Kafka_Consumer.DataBase_Connector.DataBase_Connector // Import your database connector

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
  override def processElement(
    xml: String,
    ctx: ProcessFunction[String, BusData]#Context,
    out: Collector[BusData]
  ): Unit = {
    // Parse the XML and extract relevant fields
    val busElement = scala.xml.XML.loadString(xml)
    val timestamp = busElement.attribute("timestamp").map(_.text).getOrElse("")
    val busNodes = busElement \ "bus"

    // Extract common fields from the first bus element (assuming they are the same for all)
    if (busNodes.nonEmpty) {
      val busAttributes = busNodes.head.attributes
      val dev = busAttributes("dev").text
      val lat = busAttributes("lat").text
      val lon = busAttributes("lon").text
      val head = busAttributes("head").text
      val fix = busAttributes("fix").text
      val route = busAttributes("route").text
      val stop = busAttributes("stop").text
      val next = busAttributes("next").text
      val code = busAttributes("code").text
      val fer = busAttributes("fer").text

      // Create a BusData object
      val busData = BusData(dev, timestamp, lat, lon, head, fix, route, stop, next, code, fer)

      // Send the BusData object to the output collector
      DataBase_Connector.send_entry(busData)
    }

  }
}
