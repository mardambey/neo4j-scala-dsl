package neo4j.dsl.clause

/** Base trait for all Cypher clauses.
 */
trait Clause {
  /** This method is used when the string version of this clause is needed
   *  in order to build a Neo4j Cypher Query.
   *
   *  @return string version of this clause
   */
  override def toString: String
}
