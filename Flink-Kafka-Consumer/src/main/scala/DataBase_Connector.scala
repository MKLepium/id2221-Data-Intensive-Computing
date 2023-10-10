package com.Flink_Kafka_Consumer.DataBase_Connector

import java.sql.{Connection, DriverManager, PreparedStatement}

import com.Flink_Kafka_Consumer.XML_Parser.BusData

object DataBase_Connector {

  def getConnection(): Connection = {
    val dbUrl = "jdbc:postgresql://127.0.0.1:5432/bus_data"
    val dbUser = "postgres"
    val dbPassword = "db_password"
    val connection: Connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
    connection
  }

  def send_entries(busDataList: List[BusData], connection: Connection): Unit = {
    val insertSQL = """
      INSERT INTO bus_data_schema.bus_data
      (dev, time, lat, lon, head, fix, route, stop, next, code, fer)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """

    // Use a PreparedStatement to insert the data safely (to prevent SQL injection)
    val preparedStatement: PreparedStatement = connection.prepareStatement(insertSQL)

    try {

      // Iterate over the list of BusData objects and add them to the batch
      for (busData <- busDataList) {
        preparedStatement.setString(1, busData.dev)
        preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(busData.time))
        preparedStatement.setBigDecimal(3, new java.math.BigDecimal(busData.lat))
        preparedStatement.setBigDecimal(4, new java.math.BigDecimal(busData.lon))
        preparedStatement.setString(5, busData.head)
        preparedStatement.setString(6, busData.fix)
        preparedStatement.setString(7, busData.route)
        preparedStatement.setString(8, busData.stop)
        preparedStatement.setString(9, busData.next)
        preparedStatement.setString(10, busData.code)
        preparedStatement.setString(11, busData.fer)

        // Add the current statement to the batch
        preparedStatement.addBatch()
      }

      // Execute the batch insert
      preparedStatement.executeBatch()

      // Commit the transaction
      //connection.commit()
    } catch {
      case e: Exception =>
        println("Error inserting data into database")
        e.printStackTrace()
        // Rollback the transaction in case of an error
        connection.rollback()
    } finally {
      preparedStatement.close()
    }
  }
}
