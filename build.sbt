name := "sealed-trait-event-processor"

version := "0.1"

scalaVersion in ThisBuild := "2.13.4"
run := run in Compile in `event-handler`

lazy val `event-processor` = (project in file("event-processor")).settings(
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

lazy val `event-handler` = (project in file("event-handler"))settings(
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.2" % "test",
    "org.scalatest" %% "scalatest-funsuite" % "3.2.2" % "test"
  )
  ) dependsOn `event-processor`
