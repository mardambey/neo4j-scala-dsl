package neo4j.dsl.clause

import neo4j.dsl.graph.{ Key, Identifier, GraphObject }
import neo4j.dsl.StringConstants
import neo4j.dsl.predicate.Predicate
import neo4j.dsl.pattern.Pattern

class StringOrKeyOrIdentifierOrPredicateOrPattern[T]
object StringOrKeyOrIdentifierOrPredicateOrPattern {
  implicit object KeyWitness extends StringOrKeyOrIdentifierOrPredicateOrPattern[Key[_, _]]
  implicit object IdentifierWitness extends StringOrKeyOrIdentifierOrPredicateOrPattern[Identifier[_]]
  implicit object StringWitness extends StringOrKeyOrIdentifierOrPredicateOrPattern[String]
  implicit object PredicateWitness extends StringOrKeyOrIdentifierOrPredicateOrPattern[Predicate]
  implicit object PatternWitness extends StringOrKeyOrIdentifierOrPredicateOrPattern[Pattern]
}

/** Constructs a ReturnExpression that may be comprised of a Key, Identifer, or straigh up String.
 *  @param expression the expression to return
 *  @tparam ExpressionType the type of the expression, see StringOrKeyOrIdentifer for mor information.
 */
case class ReturnExpression[ExpressionType: StringOrKeyOrIdentifierOrPredicateOrPattern](expression: ExpressionType) {
  override def toString: String = {
    expression match {
      case k: Key[_, _]     ⇒ k.owner.__.toString + "." + k // FIXME: should this stay here or move somewhere more central?
      case i: Identifier[_] ⇒ i.toString
      case s: String        ⇒ s
      case p: Predicate     ⇒ p.toString
      case p: Pattern       ⇒ p.toString
    }
  }
}

trait ReturnConstants {
  /** A special ReturnExpression indicating that everything ("*") should be returned.
   *  @return the everything ("*") ReturnExpression
   */
  def * : ReturnExpression[String] = ReturnExpression(StringConstants.ALL)
}

trait ReturnImplicits {
  implicit def keyToReturnClause[Owner <: GraphObject[Owner]](key: Key[Owner, _]): ReturnExpression[Key[_, _]] = {
    ReturnExpression(key)
  }

  implicit def identifierToReturnClause[Owner <: GraphObject[Owner]](identifier: Identifier[Owner]): ReturnExpression[Identifier[_]] = {
    ReturnExpression(identifier)
  }

  implicit def stringToReturnExpression(string: String): ReturnExpression[String] = {
    ReturnExpression(string)
  }

  implicit def predicateToReturnExpression(predicate: Predicate): ReturnExpression[Predicate] = {
    ReturnExpression(predicate)
  }

  implicit def patternToReturnExpression(pattern: Pattern): ReturnExpression[Pattern] = {
    ReturnExpression(pattern)
  }
}

/** Constructs a Cypher RETURN clause given one or more ReturnExpressions.
 *  @param returnExpression one or more ReturnExpressions to return by this Return clause.
 */
case class Return(returnExpression: ReturnExpression[_]*) extends Clause {
  override def toString: String = {
    "RETURN " + returnExpression.mkString(",")
  }
}
