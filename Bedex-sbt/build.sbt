import AssemblyKeys._ // put this at the top of the file

assemblySettings

// mainClass in assembly := Some("bedex.BedexApp")
mainClass in assembly := Some("bedex.gui.BedexMainApp")

name := "bedex"

version := "0.3-SNAPSHOT"

scalaVersion := "2.10.2"

libraryDependencies += "org.scalafx" %% "scalafx" % "1.0.0-M4"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.5"

libraryDependencies +=  "org.scala-lang" % "scala-actors" % "2.10.2"

// local resources (used for production only)

resolvers += "My Repo" at "http://repo:8080/archiva/repository/internal"

libraryDependencies += "oracle" % "ojdbc5" % "11.2.0.2.0"

// tests

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0.M6-SNAP36" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"

libraryDependencies += "com.h2database" % "h2" % "1.3.173"

// JavaFX

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
    cp filter {_.data.getName == "jfxrt.jar"}
}

fork in run := true

autoScalaLibrary := false

scalacOptions ++= Seq( "-unchecked", "-deprecation", "-feature" )

// signing the webstart jar

TaskKey[Unit]("sign_jar") := {
	// FIXME hardcoded
	val art = "bedex-assembly-0.3-SNAPSHOT"
	val ksFile = System.getenv("KEYSTORE_FILE")
	val ksPassword = System.getenv("KEYSTORE_PASSWORD")
	val ksAlias = System.getenv("KEYSTORE_ALIAS")
	val originalJar = "target/scala-2.10/" + art + ".jar"
	val signedJar = "target/scala-2.10/" + art + "-signed.jar"
	//val command = "jarsigner -keystore " + ksFile + " -signedjar " + signedJar + " " + originalJar + " " + ksAlias + ""
	val command = "jarsigner -keystore %s -signedjar %s %s -storepass %s %s".format(ksFile, signedJar, originalJar, ksPassword, ksAlias)
	println("Running: " + command)
	command !
}	
