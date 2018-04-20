package utils

import org.scalatest.{BeforeAndAfterEach, Suite}
import play.api.db.Database
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.Tables
import slick.jdbc.SetParameter.SetUnit
import slick.jdbc.{JdbcProfile, SQLActionBuilder}
import utils.extensions._

import scala.util.Try

trait DatabaseCleanerOnEachTest
  extends HasDatabaseConfigProvider[JdbcProfile] with BeforeAndAfterEach {
  this: Suite with InMemoryDatabaseFlatSpec =>

  override lazy val dbConfigProvider: DatabaseConfigProvider = app.injector.instanceOf[DatabaseConfigProvider]
  lazy val database: Database = app.injector.instanceOf[Database]

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    clearDatabase()
  }

  override protected def afterEach(): Unit = {
    clearDatabase()
    super.afterEach()
  }

  def clearDatabase(): Unit = {
    Try(dropTables())
    createTables()
  }

  private def createTables() = {
    import dbConfig.profile.api._
    val createSchemaQuery = sqlu"""CREATE SCHEMA IF NOT EXISTS "tpf""""
    db.run(createSchemaQuery).awaitForResult
    val createSchemaQuery2 = sqlu"""CREATE SCHEMA IF NOT EXISTS "TPF""""
    db.run(createSchemaQuery2).awaitForResult
    println("-------->")

    Tables.schema.createStatements.toList.foreach { query =>
      println(query)
      db.run(SQLActionBuilder(List(query), SetUnit).asUpdate).awaitForResult
    }
    println("-------->")
  }

  private def dropTables() = {
    Tables.schema.dropStatements.toList.reverse.foreach { query =>
      db.run(SQLActionBuilder(List(query), SetUnit).asUpdate).awaitForResult
    }
  }

}
