val scala3Version = "3.3.1"
val AkkaVersion = "2.9.0-M2"
val AkkaHttpVersion = "10.6.0-M1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "house-rent-predicter",
    version := "0.1.1-SNAPSHOT",

    scalaVersion := scala3Version,

	// Apache Spark dependencies
	libraryDependencies ++= Seq(
		"org.pmml4s" %%  "pmml4s" % "1.0.1",
		  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
		  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
		  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
		   "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
	)
  )


