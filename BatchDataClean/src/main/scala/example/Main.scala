import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types._


// translate the schema to a case class
case class BusData(dev: String, time: String, lat: Double, lon: Double, head: String, fix: String, route: String, stop: String, next: String, code: String, fer: String)


object DatabaseToDataFrame {
  def getDataFromPostgres(): DataFrame = {
    // Create a SparkSession
    val spark = SparkSession.builder()
      .appName("DatabaseToDataFrame").master("local[*]")
      .getOrCreate()

    // Define the database connection properties
    val dbUrl = "jdbc:postgresql://127.0.0.1:5432/bus_data"
    val dbUser = "postgres"
    val dbPassword = "db_password"
    val tableName = "bus_data_schema.bus_data"

    // Using the case class read from db
    // 
    val df = spark.read
      .format("jdbc")
      .option("url", dbUrl)
      .option("user", dbUser)
      .option("password", dbPassword)
      .option("dbtable", tableName)
      .load()
      .as[BusData]
      .toDF()


    // Stop the SparkSession
    spark.stop()


    // Return the DataFrame
    df
  }

  def main(args: Array[String]): Unit = {
    val dfFromDatabase = getDataFromPostgres()

    // Use dfFromDatabase as needed (e.g., apply transformations or show data)
    dfFromDatabase.show()
  }
}