package neo4j.dsl

package object function {

  def ALL[IdentifierType <: ConstantValueIdentifier](functionExpression: AggregateFunctionExpression[IdentifierType]): All[IdentifierType] = {
    All(functionExpression)
  }

  def NONE[IdentifierType <: ConstantValueIdentifier](functionExpression: AggregateFunctionExpression[IdentifierType]): None[IdentifierType] = {
    None(functionExpression)
  }

}
