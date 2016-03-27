package neo4j.dsl

import neo4j.dsl.graph.Identifier
import neo4j.dsl.pattern.Pattern
import neo4j.dsl.predicate.Predicate

package object clause {

  /** Generates a Cypher MATCH clause.
   *  @param pattern pattern used to generate the MATCH
   *  @param statement Statement this MATCH belongs to
   */
  def MATCH(pattern: Pattern)(implicit statement: Statement): Match = {
    val mtch = Match(pattern)
    statement.addClause(mtch)
    mtch
  }

  /** Generates a Cypher MERGE clause.
   *  @param pattern pattern used to generate the MERGE
   *  @param statement Statement this MERGE belongs to
   */
  def MERGE(pattern: Pattern)(implicit statement: Statement): Unit = {
    val merge = Merge(pattern)
    statement.addClause(merge)
  }

  /** Generates a RETURN clause.
   *
   *  @param returnClause one or more ReturnExpression instances
   *  @param statement Statement this RETURN belongs to
   */
  def RETURN(returnClause: ReturnExpression[_]*)(implicit statement: Statement): Unit = {
    val ret = Return(returnClause: _*)
    statement.addClause(ret)
  }

  /** Generates a WHERE clause.
   *
   *  @param predicate Predicate for this WHERE clause
   *  @param statement Statement this WHERE belongs to
   */
  def WHERE(predicate: Predicate)(implicit statement: Statement): Where = {
    val where = Where(predicate)
    statement.addClause(where)
    where
  }

  def DELETE(identifier: Identifier[_]*)(implicit statement: Statement): Unit = {
    val delete = Delete(identifier: _*)
    statement.addClause(delete)
  }

  /** Generates a WITH clause.
   *  @param withExpression one or more WithExpression instances
   *  @param statement Statement this WITH belongs to
   */
  def WITH(withExpression: WithExpression[_]*)(implicit statement: Statement): Unit = {
    val wth = With(withExpression: _*)
    statement.addClause(wth)
  }
}
