name := "simplePost"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings
