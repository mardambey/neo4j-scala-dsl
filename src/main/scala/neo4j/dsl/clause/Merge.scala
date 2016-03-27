package neo4j.dsl.clause

import neo4j.dsl.pattern.Pattern

/** Constructs a MERGE clause with the given Pattern.
 *
 *  @param pattern a Pattern object for this MERGE clause, usually built implicitly through Pattern matching operators (see Pattern).
 */
case class Merge(pattern: Pattern) extends Clause {
  override def toString: String = {
    "MERGE " + pattern
  }
}
