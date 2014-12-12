name := "AkkaPatternAsk"
version := "1.0"

scalaVersion := "2.11.4"
scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= {
	val akkaVersion = "2.3.7"
	Seq(
		"com.github.nscala-time" %% "nscala-time" % "1.4.0",
		"com.typesafe.akka" %% "akka-actor" % akkaVersion,
		"com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
		"ch.qos.logback" % "logback-classic" % "1.1.2"
	)
}

updateOptions := updateOptions.value.withCachedResolution(true)

Revolver.settings
javaOptions in Revolver.reStart += "-Dfile.encoding=utf8"
