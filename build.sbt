ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"
Global / onChangedBuildSource := IgnoreSourceChanges

lazy val root = (project in file("."))
  .settings(
    name := "Smithy4sTodo",
    libraryDependencies ++= Seq(
      "com.disneystreaming.smithy4s" %% "smithy4s-http4s" % smithy4sVersion.value,
      "com.disneystreaming.smithy4s" %% "smithy4s-http4s-swagger" % smithy4sVersion.value,
      "org.typelevel" %% "cats-effect" % "3.4.7",
      "ch.qos.logback" % "logback-classic" % "1.4.5",
      "org.http4s" %% "http4s-ember-server" % "0.23.16"
    )
  )
  .enablePlugins(Smithy4sCodegenPlugin, JavaAppPackaging)
