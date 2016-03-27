package neo4j.dsl.graph

import neo4j.dsl.clause.QueryContext

import scala.reflect.runtime.{ currentMirror ⇒ cm }

/** A GraphObject is the base class for a Node or Relation object. It holds onto KeyExpressions as
 *  well as an internal Identifer for uniquely identifies it's instance in Cypher statements.
 *
 *  @tparam Owner this object's owner type.
 */
abstract class GraphObject[Owner <: GraphObject[Owner]] {
  private lazy val _name: String = { cm.reflect(this).symbol.name.toTypeName.decodedName.toString }

  var keyExpressions: Option[Seq[KeyExpression[Owner, _]]] = None
  var identifier = Identifier[Owner]("_", this)

  /** Returns the uniquely associated Identifer for this object, or "_" if none has been assigned yet.
   *  @return this object's Identifier.
   */
  def __ : Identifier[Owner] = identifier

  /** Creates a GraphObject (Node or Relation) given KeyExpression objects.
   *  @param exprBuilder the expression builder used to build this object
   *  @param m the owner's manifest
   *  @param context an implicit QueryContext, used to assign an Identifier to this object
   *  @return the GraphObject itself.
   */
  def apply(exprBuilder: (Owner ⇒ KeyExpression[Owner, _])*)(implicit m: Manifest[Owner], context: QueryContext): Owner = {
    val obj = m.runtimeClass.newInstance().asInstanceOf[Owner]

    if (exprBuilder.nonEmpty) {
      val keyExpressions = exprBuilder.map(_(obj))
      obj.keyExpressions = Some(keyExpressions)
    }

    obj.identifier = context.nextIdentifier(obj)

    obj
  }

  def copy(identifier: String)(implicit m: Manifest[Owner]): Owner = {
    val obj = m.runtimeClass.newInstance().asInstanceOf[Owner]
    obj.identifier = Identifier(identifier, obj)
    obj
  }

  /** Returns the GraphObject's (Node or Relation) name, for example: User, Post, WorksForRelation, etc.
   *  @return the GraphObject's name.
   */
  def objectName: String = _name

  override def toString: String = {
    val keyExprs = keyExpressions
      .map(_.map(_.toString())) // get string form: k1: v1
      .map(_.mkString(", ")) // join with commas: k1: v1, k2: v2
      .map(" {" + _ + "}") // wrap with braces: { k1:v1, k2:v2 }, note that we add a leading space
      .getOrElse("") // otherwise, empty string

    s"$identifier:$objectName$keyExprs"
  }
}

