package com.Flink_Kafka_Consumer

import java.util.Properties
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.common.serialization.SimpleStringSchema


import java.sql.{Connection, DriverManager, ResultSet}
import com.Flink_Kafka_Consumer.XML_Parser.ParseXMLFunction
import com.Flink_Kafka_Consumer.XML_Parser.BusData
import java.util.UUID

object Main extends App {
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val properties = new Properties()
  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("group.id", "0")
  val kafkaConsumer = new FlinkKafkaConsumer[String]("xml-data", new SimpleStringSchema(), properties)



  val stream = env
    .addSource(kafkaConsumer)
    
  val busDataStream = stream
    .process(ParseXMLFunction)


  env.execute("Flink Kafka Consumer")
}
