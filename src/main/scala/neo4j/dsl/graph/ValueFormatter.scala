package neo4j.dsl.graph

/** A ValueFormatter knows how to format a value so that it is properly rendered and
 *  encoded in a Cypher query.
 *  @tparam T the type of the value
 */
trait ValueFormatter[T] {
  def format(value: T): String
}

object DefaultFormatters {

  class IntegerFormatter extends ValueFormatter[Int] {
    override def format(value: Int): String = value.toString
  }

  class DoubleFormatter extends ValueFormatter[Double] {
    override def format(value: Double): String = value.toString
  }

  class BooleanFormatter extends ValueFormatter[Boolean] {
    override def format(value: Boolean): String = if (value) "TRUE" else "FALSE"
  }

  class StringFormatter extends ValueFormatter[String] {
    override def format(value: String): String = s"'$value'"
  }

  class ListStringFormatter extends ValueFormatter[List[String]] {
    override def format(value: List[String]): String = s"['${value.mkString("','")}']"
  }

  class MapStringFormatter extends ValueFormatter[Map[String, String]] {
    override def format(value: Map[String, String]): String = s"['${value.mkString("','")}']"
  }
}
