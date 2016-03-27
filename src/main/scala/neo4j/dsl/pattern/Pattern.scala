package neo4j.dsl.pattern

import neo4j.dsl.clause.QueryContext
import neo4j.dsl.graph.{ Identifier, Relation, Node, GraphObject }

/** Constructs a Cypher pattern given Node and Relation objects.
 *
 *  @param parts the Node and Relation objects that are forming this Pattern.
 */
case class Pattern(var parts: List[Any] = List.empty[Any]) {

  // Identifier methods
  def :--:[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): Pattern = {
    Pattern(identifier +: NodeDirection.Both +: parts)
  }

  def :<--:[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): Pattern = {
    Pattern(identifier +: NodeDirection.Both +: parts)
  }

  def :-->:[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): Pattern = {
    Pattern(identifier +: NodeDirection.Right +: parts)
  }

  def :-:[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): Pattern = {
    Pattern(identifier +: RelationDirection.Both +: parts)
  }

  def :<-:[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): Pattern = {
    Pattern(identifier +: RelationDirection.Both +: parts)
  }

  def :->:[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): Pattern = {
    Pattern(identifier +: RelationDirection.Right +: parts)
  }

  // Node methods
  def :--:[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]): Pattern = {
    Pattern(graphObject +: NodeDirection.Both +: parts)
  }

  def :<--:[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]): Pattern = {
    Pattern(graphObject +: NodeDirection.Both +: parts)
  }

  def :-->:[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]): Pattern = {
    Pattern(graphObject +: NodeDirection.Right +: parts)
  }

  // Relation methods
  def :-:[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]): Pattern = {
    Pattern(graphObject +: RelationDirection.Both +: parts)
  }

  def :<-:[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]): Pattern = {
    Pattern(graphObject +: RelationDirection.Both +: parts)
  }

  def :->:[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]): Pattern = {
    Pattern(graphObject +: RelationDirection.Right +: parts)
  }

  override def toString: String = {
    parts.map {
      case n: Node[_]       ⇒ "(" + n + ")"
      case r: Relation[_]   ⇒ "[" + r + "]"
      case i: Identifier[_] ⇒ "(" + i + ")"
      case d: Direction     ⇒ d
    }.mkString
  }
}

trait PatternImplicits {
  implicit def graphObjectToPattern[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]): Pattern = {
    new Pattern(List(graphObject))
  }

  implicit def identifierToPattern[Owner <: GraphObject[Owner]](identifier: Identifier[Owner])(implicit context: QueryContext): Pattern = {
    Pattern(List(identifier))
  }
}

