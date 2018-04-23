package developers.storage

import java.util.UUID

import developers.model.Developer
import slick.Tables._

object codec {

  implicit def asRow(developer: Developer): DevelopersRow =
    DevelopersRow(developer.id.toString, developer.username, developer.email)

  implicit def asDomain(row: DevelopersRow): Developer =
    Developer(UUID.fromString(row.id), row.username, row.email)

  implicit def asDomain(maybeRow: Option[DevelopersRow]): Option[Developer] = maybeRow.map(asDomain)
}
