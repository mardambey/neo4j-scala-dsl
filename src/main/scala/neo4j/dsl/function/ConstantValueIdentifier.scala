package neo4j.dsl.function

import neo4j.dsl.clause.{ Match, QueryContext }
import neo4j.dsl.graph.GraphObject
import neo4j.dsl.predicate.Predicate

trait ConstantValueIdentifier {
  // the label used within the function to refer to the pathIdentifier: ALL (xx IN nodes(p) WHERE xx.age > 30)
  val identifier: String = "xx"
}

trait PathIdentifier extends ConstantValueIdentifier {

  val queryContext: QueryContext

  // the label for the MATCH itself: MATCH p=(a)-[*1..3]->(b)
  var matchIdentifier: String = _
}

class PathIdentifier1[Owner <: GraphObject[Owner]](pathIdentifier: Owner)(
  implicit override val queryContext: QueryContext,
  m: Manifest[Owner])
    extends PathIdentifier {

  private val _owner = pathIdentifier.copy(identifier)

  def _1: Owner = _owner
}

class PathIdentifier2[Owner1 <: GraphObject[Owner1], Owner2 <: GraphObject[Owner2]](pathIdentifier: (Owner1, Owner2))(
  implicit override val queryContext: QueryContext,
  m1: Manifest[Owner1],
  m2: Manifest[Owner2])
    extends PathIdentifier {

  private val _owner1 = pathIdentifier._1.copy(identifier)
  private val _owner2 = pathIdentifier._2.copy(identifier)

  def _1: Owner1 = _owner1
  def _2: Owner2 = _owner2
}

trait Alias extends ConstantValueIdentifier {

  val queryContext: QueryContext

  // the label used within the function to refer to the alias: ALL (x IN nodes(p) WHERE x.age > 30)
  var aliasIdentifier: String = _
}

class Alias1[Owner <: GraphObject[Owner]](alias: Owner)(
  implicit override val queryContext: QueryContext,
  m: Manifest[Owner])
    extends Alias {

  private val _owner = alias.copy(identifier)

  def _1: Owner = _owner
}

class Alias2[Owner1 <: GraphObject[Owner1], Owner2 <: GraphObject[Owner2]](alias: (Owner1, Owner2))(
  implicit override val queryContext: QueryContext,
  m1: Manifest[Owner1],
  m2: Manifest[Owner2])
    extends Alias {

  private val _owner1 = alias._1.copy(identifier)
  private val _owner2 = alias._2.copy(identifier)

  def _1: Owner1 = _owner1
  def _2: Owner2 = _owner2
}

trait IdentifierImplicits {

  def p[Owner <: GraphObject[Owner]](pathIdentifier: Owner)(implicit queryContext: QueryContext, m: Manifest[Owner]): PathIdentifier = new PathIdentifier1(pathIdentifier)
  def p[Owner1 <: GraphObject[Owner1], Owner2 <: GraphObject[Owner2]](tuple: (Owner1, Owner2))(implicit queryContext: QueryContext, m1: Manifest[Owner1], m2: Manifest[Owner2]): PathIdentifier2[Owner1, Owner2] = new PathIdentifier2(tuple)

  def a[Owner <: GraphObject[Owner]](alias: Owner)(implicit queryContext: QueryContext, m: Manifest[Owner]): Alias1[Owner] = new Alias1(alias)
  def a[Owner1 <: GraphObject[Owner1], Owner2 <: GraphObject[Owner2]](tuple: (Owner1, Owner2))(implicit queryContext: QueryContext, m1: Manifest[Owner1], m2: Manifest[Owner2]): Alias2[Owner1, Owner2] = new Alias2(tuple)

  implicit class PathIdentifierImplicits(identifier: PathIdentifier) {
    def IN(collection: Collection[PathIdentifier], predicate: Predicate): AggregateFunctionExpression[PathIdentifier] = {
      AggregateFunctionExpression(identifier, collection, predicate)
    }

    def :=(mtch: Match): PathIdentifier = {
      identifier.matchIdentifier = identifier.queryContext.nextIdentifier
      mtch.pathIdentifier = Some(identifier)
      identifier
    }
  }

  implicit class AliasImplicits(identifier: Alias) {
    def IN(collection: Collection[Alias], predicate: Predicate): AggregateFunctionExpression[Alias] = {
      AggregateFunctionExpression(identifier, collection, predicate)
    }
  }
}
