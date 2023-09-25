import java.util.Properties
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.common.serialization.SimpleStringSchema



object Main extends App {
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val properties = new Properties()
  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("group.id", "test")
  val stream = env
    .addSource(new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties))
    
  val busDataStream: DataStream[BusData] = stream
    .process(new ParseXMLFunction)

  busDataStream.print()
  stream.print()
  env.execute("Flink Kafka Consumer")
  println("Hello, World!")
}