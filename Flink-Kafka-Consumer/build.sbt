scalaVersion := "2.13.8"

// Maybe I'll modify this later.
name := "hello-world2"
organization := "ch.epfl.scala"
version := "1.0"


libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"


libraryDependencies += "org.apache.flink" %% "flink-scala" % "1.9.1"
libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % "1.9.1"
libraryDependencies += "org.apache.flink" %% "flink-connector-kafka" % "1.9.1"


libraryDependencies += "org.postgresql" % "postgresql" % "42.6.0"
