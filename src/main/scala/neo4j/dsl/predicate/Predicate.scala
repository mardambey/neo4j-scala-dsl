package neo4j.dsl.predicate

import neo4j.dsl.graph.{ GraphObject, Key, Identifier }
import neo4j.dsl.function.{ LiteralCollection, AggregateFunction }

trait Predicate

case class BinaryPredicate(predicate1: Predicate, operator: Operator, predicate2: Predicate) extends Predicate {

  override def toString: String = {
    predicate1.toString + " " + operator + " " + predicate2.toString
  }
}

case class IdentifierPredicate[Owner <: GraphObject[Owner]](identifier1: Identifier[Owner], operator: Operator, identifier2: Identifier[Owner])
    extends Predicate {

  override def toString: String = {
    identifier1.toString + " " + operator + " " + identifier2.toString
  }
}

case class KeyPredicate[Owner <: GraphObject[Owner], KeyType](key1: Key[Owner, KeyType], operator: Operator, key2: Key[Owner, KeyType])
    extends Predicate {

  override def toString: String = {
    key1.owner.__.toString + "." + key1 + " " + operator + " " + key1.owner.__.toString + "." + key1
  }
}

case class KeyLiteralPredicate[Owner <: GraphObject[Owner], KeyType](key1: Key[Owner, KeyType], operator: Operator, literal: KeyType)
    extends Predicate {

  override def toString: String = {
    key1.owner.__.toString + "." + key1 + " " + operator + key1._formatter.format(literal)
  }
}

case class FunctionPredicate(function: AggregateFunction) extends Predicate {
  override def toString: String = {
    function.toString()
  }
}

case class LiteralCollectionPredicate[Owner <: GraphObject[Owner], KeyType](literalCollection: LiteralCollection[Owner, KeyType]) extends Predicate {
  override def toString: String = {
    literalCollection.toString()
  }
}

trait PredicateImplicits {

  implicit def functionToPredicate(function: AggregateFunction): Predicate = {
    FunctionPredicate(function)
  }

  implicit def literalCollectionToPredicate[Owner <: GraphObject[Owner], KeyType](literalCollection: LiteralCollection[Owner, KeyType]): Predicate = {
    LiteralCollectionPredicate(literalCollection)
  }

  implicit class BinaryPredicateImplicits(predicate1: Predicate) {
    def AND(predicate2: Predicate): Predicate = {
      BinaryPredicate(predicate1, And, predicate2)
    }

    def OR(predicate2: Predicate): Predicate = {
      BinaryPredicate(predicate1, Or, predicate2)
    }
  }

  implicit class IdentifierPredicateImplicits[Owner <: GraphObject[Owner]](identifier1: Identifier[Owner]) {

    def :=(identifier2: Identifier[Owner]): Predicate = {
      IdentifierPredicate(identifier1, Equals, identifier2)
    }

    def <>(identifier2: Identifier[Owner]): Predicate = {
      IdentifierPredicate(identifier1, NotEquals, identifier2)
    }
  }

  implicit class KeyPredicates[Owner <: GraphObject[Owner], KeyType](key1: Key[Owner, KeyType]) {
    def :=(key2: Key[Owner, KeyType]): Predicate = {
      KeyPredicate(key1, Equals, key2)
    }

    def <>(key2: Key[Owner, KeyType]): Predicate = {
      KeyPredicate(key1, NotEquals, key2)
    }

    def :=(literal: KeyType): Predicate = {
      KeyLiteralPredicate(key1, Equals, literal)
    }

    def <>(literal: KeyType): Predicate = {
      KeyLiteralPredicate(key1, NotEquals, literal)
    }
  }
}

