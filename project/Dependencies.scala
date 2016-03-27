import sbt._

object Dependencies {
  val apacheCommons  = "org.apache.commons" % "commons-email" % "1.3.2"
  val config         = "com.typesafe" % "config" % "1.2.0"
  val jodaTime       = "joda-time" % "joda-time" % "2.8.2"
  val logback        = "ch.qos.logback" % "logback-classic" % "1.1.1"
  val mockito        = "org.mockito" % "mockito-core" % "1.8.5"
  val playJson       = "com.typesafe.play" %% "play-json" % "2.4.6"
  val playWs         = "com.typesafe.play" %% "play-ws" % "2.4.6"
  val scalaReflect   = "org.scala-lang" % "scala-reflect" % "2.11.5"
  val scalaTest      = "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  val scalaTestPlus  = "org.scalatestplus" %% "play" % "1.4.0-M4" % "test"
  val typesafeConfig = "com.typesafe" % "config" % "1.2.1"
}

