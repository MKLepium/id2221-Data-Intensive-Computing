scalaVersion := "2.12.11"


libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"

// Apache Flink dependencies-test-     
val flinkVersion = "1.14.6"
libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-clients" % flinkVersion,
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-connector-kafka" % flinkVersion,
  "org.apache.flink" %% "flink-connector-kafka-base" % "1.11.6"
)


libraryDependencies += "org.postgresql" % "postgresql" % "42.6.0"
