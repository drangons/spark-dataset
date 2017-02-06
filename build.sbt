name := "spark-dataset"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

val sparkVersion = "2.1.0"

// additional libraries
libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % s"${sparkVersion}" % "provided",
	"org.apache.spark" %% "spark-sql" % s"${sparkVersion}",
	"com.holdenkarau" %% "spark-testing-base" % "0.6.0" % "test"
)

resolvers ++= Seq(
	"JBoss Repository" at "http://repository.jboss.org/nexus/content/repositories/releases/",
	"Spray Repository" at "http://repo.spray.cc/",
	"Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
	"Akka Repository" at "http://repo.akka.io/releases/",
	"Twitter4J Repository" at "http://twitter4j.org/maven2/",
	"Apache HBase" at "https://repository.apache.org/content/repositories/releases",
	"Twitter Maven Repo" at "http://maven.twttr.com/",
	"scala-tools" at "https://oss.sonatype.org/content/groups/scala-tools",
	"Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
	"Second Typesafe repo" at "http://repo.typesafe.com/typesafe/maven-releases/",
	"Mesosphere Public Repository" at "http://downloads.mesosphere.io/maven",
	Resolver.sonatypeRepo("public")
)

assemblyMergeStrategy in assembly := {
	case PathList("javax", "inject", xs@_*) => MergeStrategy.last
	case PathList("org", "aopalliance", xs@_*) => MergeStrategy.last
	case PathList("org", "apache", xs@_*) => MergeStrategy.last
	case "about.html" => MergeStrategy.rename
	case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
	case "META-INF/mailcap" => MergeStrategy.last
	case "META-INF/mimetypes.default" => MergeStrategy.last
	case "plugin.properties" => MergeStrategy.last
	case "log4j.properties" => MergeStrategy.last
	case x =>
		val oldStrategy = (assemblyMergeStrategy in assembly).value
		oldStrategy(x)
}
