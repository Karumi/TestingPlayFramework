package slick
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema
    : profile.SchemaDescription = DevelopersTable.schema ++ FlywaySchemaHistoryTable.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table DevelopersTable
    *  @param id Database column id SqlType(VARCHAR), PrimaryKey, Length(36,true)
    *  @param username Database column username SqlType(VARCHAR), Length(255,true)
    *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
  case class DevelopersRow(id: String, username: String, email: Option[String] = None)

  /** GetResult implicit for fetching DevelopersRow objects using plain SQL queries */
  implicit def GetResultDevelopersRow(implicit e0: GR[String],
                                      e1: GR[Option[String]]): GR[DevelopersRow] = GR { prs =>
    import prs._
    val r = (<<[String], <<[String], <<?[String])
    import r._
    DevelopersRow.tupled((_1, _2, _3)) // putting AutoInc last
  }

  /** Table description of table developers. Objects of this class serve as prototypes for rows in queries. */
  class DevelopersTable(_tableTag: Tag)
      extends profile.api.Table[DevelopersRow](_tableTag, None, "developers") {
    def * = (id, username, email) <> (DevelopersRow.tupled, DevelopersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(id), Rep.Some(username), email).shaped.<>({ r =>
        import r._; _1.map(_ => DevelopersRow.tupled((_1.get, _2.get, _3)))
      }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(VARCHAR), PrimaryKey, Length(36,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(36, varying = true))

    /** Database column username SqlType(VARCHAR), Length(255,true) */
    val username: Rep[String] = column[String]("username", O.Length(255, varying = true))

    /** Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
    val email: Rep[Option[String]] =
      column[Option[String]]("email", O.Length(255, varying = true), O.Default(None))
  }

  /** Collection-like TableQuery object for table DevelopersTable */
  lazy val DevelopersTable = new TableQuery(tag => new DevelopersTable(tag))

  /** Entity class storing rows of table FlywaySchemaHistoryTable
    *  @param installedRank Database column installed_rank SqlType(INT), PrimaryKey
    *  @param version Database column version SqlType(VARCHAR), Length(50,true), Default(None)
    *  @param description Database column description SqlType(VARCHAR), Length(200,true)
    *  @param `type` Database column type SqlType(VARCHAR), Length(20,true)
    *  @param script Database column script SqlType(VARCHAR), Length(1000,true)
    *  @param checksum Database column checksum SqlType(INT), Default(None)
    *  @param installedBy Database column installed_by SqlType(VARCHAR), Length(100,true)
    *  @param installedOn Database column installed_on SqlType(TIMESTAMP)
    *  @param executionTime Database column execution_time SqlType(INT)
    *  @param success Database column success SqlType(BIT) */
  case class FlywaySchemaHistoryRow(installedRank: Int,
                                    version: Option[String] = None,
                                    description: String,
                                    `type`: String,
                                    script: String,
                                    checksum: Option[Int] = None,
                                    installedBy: String,
                                    installedOn: java.sql.Timestamp,
                                    executionTime: Int,
                                    success: Boolean)

  /** GetResult implicit for fetching FlywaySchemaHistoryRow objects using plain SQL queries */
  implicit def GetResultFlywaySchemaHistoryRow(implicit e0: GR[Int],
                                               e1: GR[Option[String]],
                                               e2: GR[String],
                                               e3: GR[Option[Int]],
                                               e4: GR[java.sql.Timestamp],
                                               e5: GR[Boolean]): GR[FlywaySchemaHistoryRow] = GR {
    prs =>
      import prs._
      val r = (<<[Int],
               <<?[String],
               <<[String],
               <<[String],
               <<[String],
               <<?[Int],
               <<[String],
               <<[java.sql.Timestamp],
               <<[Int],
               <<[Boolean])
      import r._
      FlywaySchemaHistoryRow
        .tupled((_1, _2, _3, _4, _5, _6, _7, _8, _9, _10)) // putting AutoInc last
  }

  /** Table description of table flyway_schema_history. Objects of this class serve as prototypes for rows in queries.
    *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class FlywaySchemaHistoryTable(_tableTag: Tag)
      extends profile.api.Table[FlywaySchemaHistoryRow](_tableTag, None, "flyway_schema_history") {
    def * =
      (installedRank,
       version,
       description,
       `type`,
       script,
       checksum,
       installedBy,
       installedOn,
       executionTime,
       success) <> (FlywaySchemaHistoryRow.tupled, FlywaySchemaHistoryRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(installedRank),
       version,
       Rep.Some(description),
       Rep.Some(`type`),
       Rep.Some(script),
       checksum,
       Rep.Some(installedBy),
       Rep.Some(installedOn),
       Rep.Some(executionTime),
       Rep.Some(success)).shaped.<>(
        { r =>
          import r._;
          _1.map(
            _ =>
              FlywaySchemaHistoryRow.tupled(
                (_1.get, _2, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))
        },
        (_: Any) => throw new Exception("Inserting into ? projection not supported.")
      )

    /** Database column installed_rank SqlType(INT), PrimaryKey */
    val installedRank: Rep[Int] = column[Int]("installed_rank", O.PrimaryKey)

    /** Database column version SqlType(VARCHAR), Length(50,true), Default(None) */
    val version: Rep[Option[String]] =
      column[Option[String]]("version", O.Length(50, varying = true), O.Default(None))

    /** Database column description SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200, varying = true))

    /** Database column type SqlType(VARCHAR), Length(20,true)
      *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20, varying = true))

    /** Database column script SqlType(VARCHAR), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000, varying = true))

    /** Database column checksum SqlType(INT), Default(None) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum", O.Default(None))

    /** Database column installed_by SqlType(VARCHAR), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100, varying = true))

    /** Database column installed_on SqlType(TIMESTAMP) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")

    /** Database column execution_time SqlType(INT) */
    val executionTime: Rep[Int] = column[Int]("execution_time")

    /** Database column success SqlType(BIT) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (success) (database name flyway_schema_history_s_idx) */
    val index1 = index("flyway_schema_history_s_idx", success)
  }

  /** Collection-like TableQuery object for table FlywaySchemaHistoryTable */
  lazy val FlywaySchemaHistoryTable = new TableQuery(tag => new FlywaySchemaHistoryTable(tag))
}
