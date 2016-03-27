package neo4j.dsl.clause

import neo4j.dsl.StringConstants
import neo4j.dsl.function.PathIdentifier
import neo4j.dsl.pattern.Pattern

/** Constructs a(n optionally named) Cypher MATCH clause with the given Pattern.
 *
 *  @param pattern a Pattern object for this MATCH clause, usually built implicitly through Pattern matching operators (see Pattern).
 *  @param pathIdentifier an optional PathIdentifier used to generate a named path, for example: MATCH p =(michael { name:'Michael Douglas' })-->()
 */
case class Match(pattern: Pattern, var pathIdentifier: Option[PathIdentifier] = None) extends Clause {
  override def toString: String = {
    val name = pathIdentifier.map(_.matchIdentifier + StringConstants.EQUALSL) getOrElse ("")
    "MATCH " + name + pattern
  }
}
