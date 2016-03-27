package neo4j.dsl.graph

import neo4j.dsl.function.Alias

import scala.reflect.runtime.{ currentMirror ⇒ cm }

/** Base class for Key objects. See Key for more information on what a Key does.
 *
 *  @param formatter the formatter used to render the value for this Key
 *  @tparam KeyType the type of this Key's value
 */
abstract class AbstractKey[@specialized(Int, Double, Float, Long, Boolean, Short) KeyType](implicit val formatter: ValueFormatter[KeyType]) {
  lazy val name: String = cm.reflect(this).symbol.name.toTypeName.decodedName.toString
  override def toString: String = name
}

/** A Key in a Node or Relation. Keys can be thought of as the "name" part of a key / value pair.
 *
 *  @param owner the owning Node or Relation object
 *  @param _formatter the formatter used to render the value for this Key
 *  @tparam Owner the type of the owning Node or Relation object
 *  @tparam KeyType the type of this Key's value
 */
class Key[Owner <: GraphObject[Owner], KeyType](val owner: GraphObject[Owner])(implicit val _formatter: ValueFormatter[KeyType]) extends AbstractKey[KeyType] {

  /** Optionally, a key is used within an Alias
   */
  var aliasOwner: Option[Alias] = None

  /** Used to indicate that this Key's value is optional.
   *
   *  @param value the value for this key
   *  @return the KeyExpression associated with this key / value pair
   */
  def optional(value: Option[KeyType]): KeyExpression[Owner, KeyType] = {
    value match {
      case Some(v) ⇒ KeyExpression[Owner, KeyType](this, v)
      case None    ⇒ KeyExpression.empty[Owner, KeyType]
    }
  }

  /** Used to indicate that this Key's value is required, note that this is the default
   *  used by the built in apply() method.
   *
   *  @param value the value for this key
   *  @return the KeyExpression associated with this key / value pair
   */
  def required(value: KeyType): KeyExpression[Owner, KeyType] = {
    KeyExpression[Owner, KeyType](this, value)
  }

  /** Creates a required key / value pair. See required().
   *
   *  @param value the value for this key
   *  @return the KeyExpression associated with this key / value pair
   */
  def apply(value: KeyType): KeyExpression[Owner, KeyType] = required(value)
}

object KeyHelpers {

  case class StringKey[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]) extends Key[Owner, String](graphObject)
  case class IntKey[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]) extends Key[Owner, Int](graphObject)
  case class ListStringKey[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]) extends Key[Owner, List[String]](graphObject)
  case class MapStringStringKey[Owner <: GraphObject[Owner]](graphObject: GraphObject[Owner]) extends Key[Owner, Map[String, String]](graphObject)
}

