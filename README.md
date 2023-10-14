## Final project for the course "Data Intensive Computing" at KTH Royal Institute of Technology

This is the Mono repository for the final project of the course "Data Intensive Computing" at KTH Royal Institute of Technology. The project is divided into 7 parts:


# 1. Data Gathering

The data gathering part is implemented in the data_gatherer.py script. The script is run with the following command:

```bash
python3 data_gatherer.py
```

The script will gather data from the following sources:

- [straeto.is](https://straeto.is/)

The xml-data returned by the API will be taken literally and stored as a string in the Kafka Cluster.

# 2. Kafka Cluster

To enable the asynchronous communication between the different parts of the project, a Kafka cluster is used. The cluster is running on a single machine and is started with the following commands:

```bash
zookeeper-server-start.sh Kafka/zookeeper.properties
```

```bash
kafka-server-start.sh Kafka/server.properties
```

This project only provides the server.properties for the Kafka cluster, aswell as the zookeeper.properties for the Zookeeper server. The Kafka cluster is configured to run on port 9092, and the Zookeeper server is configured to run on port 2181. Feel free to change this if you want a different configuration.

# 3. Data Stream Processing

The data stream processing part is implemented in the Flink-Kafka-Consumer. It takes strings from the Kafka cluster and parses them into a case class. The case class is then used to insert the data into a PostgreSQL database. The Flink-Kafka-Consumer is started with the following command:

```bash
sbt run
```

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

What the different fields mean can be found in the [Straeto API documentation](https://straeto.is/en/about-straeto/open-data/real-time-data).


Notes:

 - We get a timestamp for each individual bus, but we only take the timestamp of the xml-response. That allows us to easier check the data for validity, but it also means that we loose some precision. We could have taken the timestamp of each individual bus, but that would have made the data validation more complicated.
 - The additional fields "dev" and "fer", are not documented in the Straeto API documentation. Dev stands for "device" and fer stands for "ferry". 

# 4. Data Storage

The data storage part is implemented in the PostgreSQL database. The sql script we used to create the database can be found in the file "db.sql". To initialize the database, run the following command:

```bash
psql -U \<username\> -f db.sql
```

# 5. (Optional) Data Deletion

The data deletion part is implemented in the BatchDataClean. It is a Work in progress Spark script that is supposed to take the elements from the DB and delete the ones that are older than X hours/ A certain timestamp. The script is started with the following command:

```bash
sbt run
```

# 6. Data Visualization

The data visualization part is implemented in two parts: The frontend and the Webserver. 

## 6.1 Webserver

The Webserver is implemented in the webserver.py script. It is a simple Flask server that serves the data from the PostgreSQL database. Essentially it is a REST-API that returns the data in JSON format. The Webserver is started with the following command:

```bash
python webserver.py
```

The API currently has one endpoint: "/bus/getData". It takes no parameters and returns the latest data for all busses in the database.

Example return:

```JSON
{
  "data": [
    [
      "Sat, 14 Oct 2023 17:47:55 GMT",
      "2-D",
      "2",
      "64.121208350",
      "-21.898571667"
    ]
  ]
}
```

## 6.2 Frontend

The frontend is implemented as a simple javascript application. It gets the data from the Webserver and displays it on a map. 