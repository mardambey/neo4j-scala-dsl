package neo4j.dsl.clause

import neo4j.dsl.function._
import neo4j.dsl.graph.{ Key, Identifier, GraphObject }

/** Represents all possible types that can create a WithExpression
 *  A WithExpression can be:
 *  - Identifier
 *  - Key
 *  - AggregateFunction
 *  - AliasedAggregateFunction
 *
 *  @tparam T the specific type of this WithExpression
 */
sealed class WithExpressionUnion[T]

object WithExpressionUnion {

  implicit object IdentifierWitness extends WithExpressionUnion[Identifier[_]]
  implicit object KeyWitness extends WithExpressionUnion[Key[_, _]]
  implicit object AggregateFunctionWitness extends WithExpressionUnion[AggregateFunction]
  implicit object AliasedAggregateFunctionWitness extends WithExpressionUnion[AliasedAggregateFunction]
}

case class WithExpression[ExpressionType: WithExpressionUnion](expression: ExpressionType) {
  override def toString: String = {
    expression match {
      case k: Key[_, _]                ⇒ k.owner.__.toString + "." + k // FIXME: should this stay here or move somewhere more central?
      case i: Identifier[_]            ⇒ i.toString
      case g: AggregateFunction        ⇒ g.toString
      case a: AliasedAggregateFunction ⇒ a.toString
    }
  }
}

trait WithImplicits {
  implicit def keyToWithExpression[Owner <: GraphObject[Owner]](key: Key[Owner, _]): WithExpression[Key[_, _]] = {
    WithExpression(key)
  }

  implicit def identifierToWithExpression[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): WithExpression[Identifier[_]] = {
    WithExpression(identifier)
  }

  implicit def aggregationFunctionToWithExpression(aggregateFunction: AggregateFunction): WithExpression[AggregateFunction] = {
    WithExpression(aggregateFunction)
  }

  implicit def aliasedAggregationFunctionToWithExpression(aliasedAggregateFunction: AliasedAggregateFunction): WithExpression[AliasedAggregateFunction] = {
    WithExpression(aliasedAggregateFunction)
  }
}

/** Generates a With Clause given one or more WithExpression instances.
 *
 *  @param withExpression used to generate the With clause
 */
case class With(withExpression: WithExpression[_]*) extends Clause {
  override def toString: String = {
    "WITH " + withExpression.mkString(",")
  }
}
