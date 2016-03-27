import sbt._
import Dependencies._

lazy val commonSettings = Seq(
  name := "neo4j-scala-dsl",
  version := "1.0.0",
  organization := "neo4j",
  scalaVersion := "2.11.7",
  resolvers ++= Seq(Resolver.mavenLocal,
    "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")
)

lazy val dslDeps = Seq(
  apacheCommons,
  jodaTime,
  playJson,
  playWs,
  scalaTest
)

lazy val dsl = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "neo4j-scala-dsl",
    publishArtifact in Test := false,
    parallelExecution in Test := false,
    fork in run := false,
    libraryDependencies ++= dslDeps).
  settings(Format.settings) 

