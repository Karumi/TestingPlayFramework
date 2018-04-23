name := """TestingPlayFramework"""
organization := "com.karumi"
version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, ScalafmtPlugin, FlywayPlugin)

scalaVersion := "2.12.4"

libraryDependencies ++= {
  Seq(
    guice,
    "mysql" % "mysql-connector-java" % "8.0.7-dmr",
    "com.h2database" % "h2" % "1.4.192",
    "org.flywaydb" %% "flyway-play" % "5.0.0",
    "com.typesafe.play" %% "play-slick" % "3.0.3",
    "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
    "net.codingwell" %% "scala-guice" % "4.1.0",
    "com.typesafe.slick" %% "slick-codegen" % "3.2.1",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    "org.scalacheck" %% "scalacheck" % "1.13.5" % Test,
    "com.h2database" % "h2" % "1.4.196" % Test
  )
}

lazy val databaseUrl = "jdbc:mysql://localhost/tpf?characterEncoding=UTF-8&nullNamePatternMatchesAll=true"
lazy val databaseUser = "tpf"
lazy val databasePassword = "tpf"

flywayUrl := databaseUrl
flywayUser := databaseUser
flywayPassword := databasePassword
flywayLocations := Seq("filesystem:conf/evolutions/default/")

import slick.codegen.SourceCodeGenerator
import slick.{model => m}

slickCodegenSettings
slickCodegenDatabaseUrl := databaseUrl
slickCodegenDatabaseUser := databaseUser
slickCodegenDatabasePassword := databasePassword
slickCodegenDriver := slick.driver.MySQLDriver
slickCodegenJdbcDriver := "com.mysql.cj.jdbc.Driver"
slickCodegenOutputPackage := "slick"
slickCodegenExcludedTables := Seq("schema_version")
slickCodegenOutputDir := file("./app/")
slickCodegenCodeGenerator := { (model: m.Model) =>
  new SourceCodeGenerator(model) {
    override def tableName =
      dbName => dbName.toCamelCase + "Table"

    override def Table = new Table(_) {
      table =>
      override def autoIncLastAsOption: Boolean = true

      // Use different factory and extractor functions for tables with > 22 columns
      override def factory = if (columns.size == 1) TableClass.elementType else if (columns.size <= 22) s"${TableClass.elementType}.tupled" else s"${EntityType.name}.apply"

      override def extractor = if (columns.size <= 22) s"${TableClass.elementType}.unapply" else s"${EntityType.name}.unapply"

      override def EntityType = new EntityTypeDef {
        override def code = {
          val args = columns.map(c =>
            c.default.map(v =>
              s"${c.name}: ${c.exposedType} = $v"
            ).getOrElse(
              s"${c.name}: ${c.exposedType}"
            )
          )
          val callArgs = columns.map(c => s"${c.name}")
          val types = columns.map(c => c.exposedType)

          if (classEnabled) {
            val prns = (parents.take(1).map(" extends " + _) ++ parents.drop(1).map(" with " + _)).mkString("")
            s"""case class $name(${args.mkString(", ")})$prns"""
          } else {
            s"""
/** Constructor for $name providing default values if available in the database schema. */
case class $name(${
              args.map(arg => {
                s"$arg"
              }).mkString(", ")
            })
type ${name}List = ${compoundType(types)}
object $name {
  def apply(hList: ${name}List): $name = hList match {
    case ${compoundValue(callArgs)} => new $name(${callArgs.mkString(", ")})
    case _ => throw new Exception("malformed HList")
  }
  def unapply(row: $name) = Some(${compoundValue(callArgs.map(a => s"row.$a"))})
}
          """.trim
          }
        }
      }

      override def PlainSqlMapper = new PlainSqlMapperDef {
        override def code = {
          val positional = compoundValue(columnsPositional.map(c => if (c.fakeNullable || c.model.nullable) s"<<?[${c.rawType}]" else s"<<[${c.rawType}]"))
          val dependencies = columns.map(_.exposedType).distinct.zipWithIndex.map { case (t, i) => s"""e$i: GR[$t]""" }.mkString(", ")
          val rearranged = compoundValue(desiredColumnOrder.map(i => if (columns.size > 22) s"r($i)" else tuple(i)))

          def result(args: String) = s"$factory($args)"

          val body =
            if (autoIncLastAsOption && columns.size > 1) {
              s"""
val r = $positional
import r._
${result(rearranged)} // putting AutoInc last
              """.trim
            } else {
              result(positional)
            }

          s"""
implicit def $name(implicit $dependencies): GR[${TableClass.elementType}] = GR{
  prs => import prs._
  ${indent(body)}
}
          """.trim
        }
      }

      override def TableClass = new TableClassDef {
        override def star = {
          val struct = compoundValue(columns.map(c => if (c.fakeNullable) s"Rep.Some(${c.name})" else s"${c.name}"))
          val rhs = s"$struct <> ($factory, $extractor)"
          s"def * = $rhs"
        }
        override def code = {
          val prns = parents.map(" with " + _).mkString("")
          // We force the schema to be none just to support H2 queries execution
          // the original implementation was val args = model.name.schema.map(n => s"""Some(n)""") ++ Seq("\""+model.name.table+"\"")
          val args = model.name.schema.map(n => s"""None""") ++ Seq("\""+model.name.table+"\"")
          s"""
class $name(_tableTag: Tag) extends profile.api.Table[$elementType](_tableTag, ${args.mkString(", ")})$prns {
  ${indent(body.map(_.mkString("\n")).mkString("\n\n"))}
}
        """.trim()
        }
      }

      def tails(n: Int) = {
        List.fill(n)(".tail").mkString("")
      }

    }
  }
}

addCommandAlias("format", ";scalafmt;test:scalafmt")