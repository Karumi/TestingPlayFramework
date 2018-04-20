package utils

import org.scalatest.FlatSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

object InMemoryDatabaseFlatSpec {
  private val inMemoryDatabaseConfiguration: Map[String, Any] = Map(
    "slick.dbs.default.profile"     -> "slick.jdbc.H2Profile$",
    "slick.dbs.default.db.url"      -> "jdbc:h2:mem:test;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
    "slick.dbs.default.db.user"     -> "",
    "slick.dbs.default.db.password" -> ""
  )

}
abstract class InMemoryDatabaseFlatSpec extends FlatSpec with GuiceOneAppPerSuite {

  import InMemoryDatabaseFlatSpec._

  override def fakeApplication(): Application = {
    val builder = overrideDependencies(
      new GuiceApplicationBuilder()
        .configure(inMemoryDatabaseConfiguration)
    )
    builder.build()
  }

  def overrideDependencies(application: GuiceApplicationBuilder): GuiceApplicationBuilder = {
    application
  }

}
