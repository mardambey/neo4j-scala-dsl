package neo4j.dsl.function

import neo4j.dsl.graph.{ GraphObject, Key, Identifier }

class IdentifierOrKey[T]
object IdentifierOrKey {
  implicit object KeyWitness extends IdentifierOrKey[Key[_, _]]
  implicit object IdentifierWitness extends IdentifierOrKey[Identifier[_]]
}

case class CollectExpression[ExpressionType: IdentifierOrKey](expression: ExpressionType) {
  override def toString: String = {
    expression match {
      case k: Key[_, _]     ⇒ k.owner.__.toString + "." + k // FIXME: should this stay here or move somewhere more central?
      case i: Identifier[_] ⇒ i.toString
    }
  }
}

case class Collect(collect: CollectExpression[_]) extends AggregateFunction {
  override def toString: String = {
    "collect(" + collect.toString + ")"
  }
}

trait CollectImplicits {
  implicit def keyToCollectExpression[Owner <: GraphObject[Owner]](key: Key[Owner, _]): CollectExpression[Key[_, _]] = {
    CollectExpression(key)
  }

  implicit def identifierToCollectExpression[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): CollectExpression[Identifier[_]] = {
    CollectExpression(identifier)
  }

  def collect[IdentifierType <: ConstantValueIdentifier](collectExpression: CollectExpression[_]): Collect = {
    Collect(collectExpression)
  }
}
