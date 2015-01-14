import NativePackagerKeys._
import com.typesafe.sbt.SbtNativePackager.packageArchetype

packageArchetype.java_application

name := "finatra_addressbook_example"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra" % "1.6.0",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.scalatest" % "scalatest_2.10" % "2.2.3" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"
)

resolvers +=
  "Twitter" at "http://maven.twttr.com"

scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions")

fork := true
