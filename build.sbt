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
  "-Xlint:-unused",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-language:implicitConversions"
)

name := "rec-html"
version := "0.0.1"

requireJsDomEnv in Test := true
scalaJSUseMainModuleInitializer := true
scalaJSModuleKind := ModuleKind.CommonJSModule

// Open: resources/todomvc.html
mainClass in Compile := Some("rec.isolate.examples.TodoMVC")

// Open: resources/index.html
//mainClass in Compile := Some("rec.isolate.examples.CounterExample")
//mainClass in Compile := Some("rec.isolate.examples.FieldCompare")
//mainClass in Compile := Some("rec.isolate.examples.HelloWorld")
//mainClass in Compile := Some("rec.isolate.examples.ViewPerson")
//mainClass in Compile := Some("rec.isolate.examples.Wizard")
//mainClass in Compile := Some("rec.allowconflicts.examples.PersonList")

addCompilerPlugin(
  "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "com.chuusai"   %%% "shapeless"         % "2.3.3",
  "be.tzbob"      %%% "scala-js-snabbdom" % "0.5.0",
  "org.scala-js"  %%% "scalajs-dom"       % "1.0.0",
  "org.typelevel" %%% "cats-core"         % "2.0.0",
  "org.scalatest" %%% "scalatest"         % "3.1.0" % Test,
)
