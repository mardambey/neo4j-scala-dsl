package neo4j.dsl.clause

import neo4j.dsl.predicate.Predicate

/** Constructs a Cypher WHERE clause given a Predicate.
 *
 *  @param predicate the Predicate object used in this WHERE clause. The predicate is usually implicitly built from Predicate operators.
 */
case class Where(predicate: Predicate) extends Clause {
  override def toString: String = {
    "WHERE " + predicate
  }
}
