package developers.storage

import java.util.UUID

import com.google.inject.Inject
import developers.model.Developer
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.Tables._
import developers.storage.codec._

import scala.concurrent.{ExecutionContext, Future}

class DevelopersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import dbConfig.profile.api._

  def createOrUpdate(developer: Developer): Future[Developer] =
    db.run(DevelopersTable.insertOrUpdate(developer)).map(_ => developer)

  def getById(id: UUID): Future[Option[Developer]] =
    db.run(DevelopersTable.filter(_.id === id.toString).result.headOption.map(asDomain))

}
