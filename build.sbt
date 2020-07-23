enablePlugins(ScalaJSBundlerPlugin)

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers += Resolver.bintrayRepo("webjars", "maven")
resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"

organization in ThisBuild := "be.tzbob"
ThisBuild / scalaVersion := "2.13.1"

scalacOptions in ThisBuild ++= Seq(
  "-encoding",
  "UTF-8",
  "-feature",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
)

name := "rec-html"
version := "0.0.1"

requireJsDomEnv in Test := true
scalaJSUseMainModuleInitializer := true
scalaJSModuleKind := ModuleKind.CommonJSModule


libraryDependencies ++= Seq(
  "be.tzbob"      %%% "scala-js-snabbdom" % "0.5.0",
  "org.scala-js"  %%% "scalajs-dom" % "1.0.0",
  "org.scalatest" %%% "scalatest"   % "3.1.0" % Test,
)