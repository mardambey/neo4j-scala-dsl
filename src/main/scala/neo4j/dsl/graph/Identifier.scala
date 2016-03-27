package neo4j.dsl.graph

/** An Identifier uniquely represents a GraphObject (Node or Relation) within a Cypher query.
 *  @param value actual value (string) of the Identifier
 *  @param graphObject the GraphObject this Identifer belongs to
 *  @tparam Owner the GraphObject's owner type.
 */
case class Identifier[Owner <: GraphObject[Owner]](value: String, graphObject: GraphObject[Owner]) {
  override def toString: String = value
}
