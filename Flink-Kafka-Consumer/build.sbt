scalaVersion := "2.12.11"

// Maybe I'll modify this later.
name := "hello-world2"
organization := "ch.epfl.scala"
version := "1.0"


libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"
// https://mvnrepository.com/artifact/org.apache.flink/flink-scala
libraryDependencies += "org.apache.flink" %% "flink-scala" % "1.17.1"
// https://mvnrepository.com/artifact/org.apache.flink/flink-streaming-scala
libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % "1.17.1" % "provided"
libraryDependencies += "org.apache.flink" %% "flink-connector-kafka" % "1.14.6"
// https://mvnrepository.com/artifact/org.apache.flink/flink-connector-kafka-base
libraryDependencies += "org.apache.flink" %% "flink-connector-kafka-base" % "1.11.6"
libraryDependencies += "org.postgresql" % "postgresql" % "42.6.0"
