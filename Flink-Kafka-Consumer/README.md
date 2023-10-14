# Flink Kafka Consumer

This Consumer takes data from a Kafka cluster, parses it from XML and inserts it into a PostgreSQL database. 

## Data Structure

The data structure of the case class is as follows:

```scala
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
  fer: String
)
```

## Notes:

Due to the way the Gatherer is written, the data can sometimes contain consecutive xml-strings which contain the same data/timestamp. Due to this we discard conflicting data on entering into the database.

```sql
INSERT INTO bus_data_schema.bus_data
(dev, time, lat, lon, head, fix, route, stop, next, code, fer)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
ON CONFLICT (dev, time) DO NOTHING
```