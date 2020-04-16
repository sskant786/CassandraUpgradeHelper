

name := "CassandraUpgradeHelper"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe" % "config" % "1.3.1",
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.6.0"
)