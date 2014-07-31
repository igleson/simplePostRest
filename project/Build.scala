import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "My first application"
    val appVersion      = "1.0"

    val appDependencies = Seq(

	"mysql" % "mysql-connector-java" % "5.1.18"        
      
    )

    val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
      
      resolvers += "JBoss repository" at "https://repository.jboss.org/nexus/content/repositories/",
      resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
            
    )

}
