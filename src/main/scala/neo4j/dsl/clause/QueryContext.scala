package neo4j.dsl.clause

import neo4j.dsl.graph.{ GraphObject, Identifier }

/** The QueryContext is used for assigning unique names to Identifiers within a Cypher query. It allows for
 *  the retrieval of a name as well as registering objects for 2 way name to object lookups.
 */
case class QueryContext() {
  private var identifiers: IndexedSeq[String] = (('a' to 'z') ++ ('a' to 'z' zip (1 to 24)).map(t ⇒ s"${t._1}${t._2}")).map(_.toString)
  private var aliases: Map[Any, Identifier[_]] = Map()
  private var revAliases: Map[Identifier[_], Any] = Map()

  /** Gets the next unused name.
   */
  def nextIdentifier: String = {
    val identifier = identifiers.head
    identifiers = identifiers.tail
    identifier
  }

  /** Gets the next unused name and registers the given GraphObject.
   *  @param go GraphObject to register
   *  @return the Identifier for the given GraphObject
   */
  def nextIdentifier[Owner <: GraphObject[Owner]](go: GraphObject[Owner]): Identifier[Owner] = {
    val id = identifiers.head
    val identifier = Identifier(id, go)
    identifiers = identifiers.tail
    register(go, identifier)
    identifier
  }

  private def register[Owner <: GraphObject[Owner]](go: GraphObject[Owner], identifier: Identifier[Owner]) {
    aliases = aliases + (go -> identifier)
    revAliases = revAliases + (identifier -> go)
  }

  /** Resolves the given GraphObject to an Identifier
   *  @param go GraphObject to resolve
   *  @return the Identifier associated with the GraphObject
   */
  def resolve[Owner <: GraphObject[Owner]](go: GraphObject[Owner]): Identifier[Owner] = {
    // FIXME: cast sucks, remove
    aliases(go).asInstanceOf[Identifier[Owner]]
  }

  /** Resolves the given Identifier to a GraphObject
   *  @param identifier Identifier to resolve
   *  @return the GraphObject that uses this Identifier
   */
  def resolve[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): Option[GraphObject[Owner]] = {
    // FIXME: remove ugly cast
    revAliases.get(identifier).asInstanceOf[Option[GraphObject[Owner]]]
  }

  var statements = List[Statement]()
  def getStatements: List[Statement] = statements
  def addStatement(statement: Statement): Unit = statements = statements :+ statement
}
