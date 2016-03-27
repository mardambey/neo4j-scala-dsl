package neo4j.dsl.graph

object KeyExpression {

  case class Nil[Owner <: GraphObject[Owner], T]() extends KeyExpression[Owner, T](null, null.asInstanceOf[T]) {
    override def toString: String = ""
  }

  def apply[Owner <: GraphObject[Owner], T](key: Key[Owner, T], value: T) = new KeyExpression[Owner, T](key, value)
  def empty[Owner <: GraphObject[Owner], T]: KeyExpression[Owner, T] = Nil[Owner, T]()
}

/** A KeyExpression represents a node or relation's key followed by it's value. A KeyExpression is a
 *  single key / value pair.
 *
 *  Some examples:
 *
 *   postId: '123e4567-e89b-12d3-a456-426655440000'
 *   email: 'foo@bar.com'
 *
 *  The above are 2 key expressions with the first key being postId, and the second being email,
 *  with their corresponding values.
 *
 *  @param key the name of they key
 *  @param value the value of key
 *  @tparam Owner the owning type (a Node or Relation)
 *  @tparam KeyType the type of the value of the Key involved in this KeyExpression
 */
class KeyExpression[Owner <: GraphObject[Owner], KeyType](val key: Key[Owner, KeyType], val value: KeyType) {
  override def toString: String = {
    s"""${key.name}: ${key.formatter.format(value)}"""
  }
}