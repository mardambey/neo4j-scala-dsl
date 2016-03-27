package neo4j.dsl.function

import neo4j.dsl.StringConstants
import neo4j.dsl.predicate.Predicate

trait AggregateFunction {
  def toString: String
}

case class AggregateFunctionExpression[IdentifierType <: ConstantValueIdentifier](identifier: IdentifierType, collection: Collection[IdentifierType], predicate: Predicate)

abstract class FunctionInWhere[IdentifierType <: ConstantValueIdentifier](functionName: String, functionExpression: AggregateFunctionExpression[IdentifierType]) extends AggregateFunction {
  override def toString: String = {
    s"$functionName (${functionExpression.identifier.identifier} ${StringConstants.IN} ${functionExpression.collection} WHERE ${functionExpression.predicate})"
  }
}

case class AliasedAggregateFunction(aggregateFunction: AggregateFunction, alias: Alias) {
  override def toString: String = {
    aggregateFunction.toString + " AS " + alias.aliasIdentifier
  }
}

case class All[IdentifierType <: ConstantValueIdentifier](functionExpression: AggregateFunctionExpression[IdentifierType])
  extends FunctionInWhere(StringConstants.ALLF, functionExpression)

case class None[IdentifierType <: ConstantValueIdentifier](functionExpression: AggregateFunctionExpression[IdentifierType])
  extends FunctionInWhere(StringConstants.NONE, functionExpression)

trait FunctionImplicits {

  implicit class AliasedAggregateFunctionImplicits(aggregateFunction: AggregateFunction) {
    def AS(alias: Alias): AliasedAggregateFunction = {
      AliasedAggregateFunction(aggregateFunction, alias)
    }
  }
}

