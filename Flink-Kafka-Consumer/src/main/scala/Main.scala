import java.util.Properties
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.common.serialization.SimpleStringSchema
import java.sql.{Connection, DriverManager, ResultSet}

object Main extends App {
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val properties = new Properties()
  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("group.id", "test")
  val kafkaConsumer = new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties)



  val stream = env
    .addSource(kafkaConsumer)
    
  val busDataStream: DataStream[BusData] = stream
    .process(new ParseXMLFunction)

  busDataStream.print()
  env.execute("Flink Kafka Consumer")
}
