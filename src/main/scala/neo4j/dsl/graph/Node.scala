package neo4j.dsl.graph

/** Represents a basic Cypher Node.
 *
 *  @tparam Owner this object's owner type.
 */
abstract class Node[Owner <: GraphObject[Owner]] extends GraphObject[Owner]
