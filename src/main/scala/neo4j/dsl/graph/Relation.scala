package neo4j.dsl.graph

/** Represents a Cypher Relation.
 *  @tparam Owner this object's owner type.
 */
abstract class Relation[Owner <: GraphObject[Owner]] extends GraphObject[Owner]
