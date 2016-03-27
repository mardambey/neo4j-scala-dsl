package neo4j.dsl.clause

import neo4j.dsl.graph.Identifier

/** Builds a Clause that deletes one or more identifier.
 *
 *  @param identifier one or more Identifier instances to be deleted
 */
case class Delete(identifier: Identifier[_]*) extends Clause {
  override def toString: String = {
    "DELETE " + identifier.mkString(",")
  }
}
