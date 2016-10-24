lazy val root = (project in file(".")).
  settings(
    name := "driver",
    version := "0.1"
  )

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
