package neo4j.dsl.predicate

/** Represents operators that are used to construct Predicate objects.
 */
trait Operator {
  val symbol: String
  override def toString: String = symbol
}

/** Unary Operator types extend this trait.
 */
trait UnaryOperator extends Operator

/** Binary Operator types extend this trait.
 */
trait BinaryOperator extends Operator

case object Equals extends BinaryOperator { val symbol = "=" }
case object NotEquals extends BinaryOperator { val symbol = "<>" }
case object And extends BinaryOperator { val symbol = "AND" }
case object Or extends BinaryOperator { val symbol = "OR" }
case object Not extends UnaryOperator { val symbol = "NOT" }

