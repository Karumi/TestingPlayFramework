package developers

import java.util.UUID

object model {
  case class Developer(id: UUID, username: String, email: Option[String])
}
