import java.sql.{Connection, DriverManager, PreparedStatement}



class DataBase_Connector{

  def DataBase_Connector(): Unit = {
    val dbUrl = "jdbc:postgresql://127.0.0.1:5432/bus_data"
    val dbUser = "postgres"
    val dbPassword = "db_password"
    val connection: Connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
  }



 def send_entry(bus_data: BusData, connection: Connection): Unit = {
    val insertSQL = """
        INSERT INTO bus_data_schema.bus_data
        (dev, time, lat, lon, head, fix, route, stop, next, code, fer)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """

    // Use a PreparedStatement to insert the data safely (to prevent SQL injection)
    val preparedStatement: PreparedStatement = connection.prepareStatement(insertSQL)

    try {
        preparedStatement.setString(1, bus_data.dev)
        preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(bus_data.time))
        preparedStatement.setBigDecimal(3, new java.math.BigDecimal(bus_data.lat))
        preparedStatement.setBigDecimal(4, new java.math.BigDecimal(bus_data.lon))
        preparedStatement.setString(5, bus_data.head)
        preparedStatement.setString(6, bus_data.fix)
        preparedStatement.setString(7, bus_data.route)
        preparedStatement.setString(8, bus_data.stop)
        preparedStatement.setString(9, bus_data.next)
        preparedStatement.setString(10, bus_data.code)
        preparedStatement.setString(11, bus_data.fer)

        // Execute the insert statement
        preparedStatement.executeUpdate()
    } catch {
        case e: Exception => e.printStackTrace()
    } finally {
        preparedStatement.close()
    }



 



}

