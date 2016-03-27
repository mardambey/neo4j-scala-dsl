package neo4j.dsl.function

import neo4j.dsl.StringConstants
import neo4j.dsl.graph.{ GraphObject, Key }

trait CollectionFunction[IdentifierType] {
  def toString(identifier: IdentifierType): String
}

case class Collection[IdentifierType <: ConstantValueIdentifier](function: CollectionFunction[IdentifierType], identifier: IdentifierType) {
  override def toString: String = {
    function.toString(identifier)
  }
}

case object Nodes extends CollectionFunction[PathIdentifier] {
  override def toString(pathIdentifier: PathIdentifier): String = StringConstants.NODES + "(" + pathIdentifier.matchIdentifier + ")"
}

case object Iter extends CollectionFunction[Alias] {
  override def toString(alias: Alias): String = alias.aliasIdentifier
}

case object Arr extends CollectionFunction[PathIdentifier] {
  override def toString(pathIdentifier: PathIdentifier): String = pathIdentifier.matchIdentifier + StringConstants.DOT + StringConstants.ARRAY
}

trait CollectionImplicits {
  def nodes[IdentifierType <: PathIdentifier](identifier: IdentifierType): Collection[PathIdentifier] = {
    Collection(Nodes, identifier)
  }

  def iter[IdentifierType <: Alias](alias: IdentifierType): Collection[Alias] = {
    alias.aliasIdentifier = alias.queryContext.nextIdentifier
    Collection(Iter, alias)
  }

  def arr[IdentifierType <: PathIdentifier](identifier: IdentifierType): Collection[PathIdentifier] = {
    Collection(Arr, identifier)
  }
}

case class LiteralCollection[Owner <: GraphObject[Owner], KeyType](items: Seq[KeyType], key: Key[Owner, KeyType]) {
  override def toString: String = {
    key.owner.__.toString + StringConstants.DOT + key + " " + StringConstants.IN + " " + "[" + items.map(key._formatter.format).mkString(",") + "]"
  }
}

trait LiteralCollectionImplicits {

  implicit class LiteralCollectionOps[Owner <: GraphObject[Owner], KeyType](key: Key[Owner, KeyType]) {
    def IN(items: KeyType*): LiteralCollection[Owner, KeyType] = {
      LiteralCollection(items, key)
    }
  }
}