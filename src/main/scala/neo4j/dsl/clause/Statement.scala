package neo4j.dsl.clause

/** Represents a Cypher statement. A statement contains multiple Clause objects (MATCH, MERGE, RETURN, etc.)
 *  @param statementBuilder the code the will run to produce this Statement
 *  @param context the QueryContext used to allocate identifiers that refer to Node and Relation objects within this Statement's Clause objects.
 */
case class Statement(statementBuilder: (Statement) ⇒ Unit, context: QueryContext) {

  protected var clauses = List[Clause]()

  /** Get Clauses that this Statement holds.
   */
  def getClauses: List[Clause] = clauses

  /** Adds a Clause to this Statement
   *  @param clause Clause to add
   */
  def addClause(clause: Clause): Unit = clauses = clauses :+ clause
}
