package neo4j

import neo4j.client.Neo4jRestConnection
import neo4j.dsl.clause._
import neo4j.dsl.function._
import neo4j.dsl.pattern.PatternImplicits
import neo4j.dsl.predicate.PredicateImplicits
import neo4j.client.{ CypherErrorOnlyParser, CypherResultAndErrorParser }
import play.api.libs.json.Reads

import scala.None
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

package object dsl
  extends DefaultImports
  with CollectImplicits
  with CollectionImplicits
  with CypherQuery
  with FunctionImplicits
  with IdentifierImplicits
  with LiteralCollectionImplicits
  with PatternImplicits
  with PredicateImplicits
  with ReturnConstants
  with ReturnImplicits
  with WithImplicits

trait CypherQuery {

  object Cypher {
    def apply[T](queryBuilder: (QueryContext) ⇒ Unit)(implicit connection: Neo4jRestConnection, reads: Reads[T], ec: ExecutionContext): Future[Try[Seq[T]]] = {

      val context = new QueryContext
      queryBuilder(context)

      val stringStatements = context.getStatements.map(statement ⇒ {
        statement.statementBuilder(statement)
        statement.getClauses.map(_.toString).mkString("\n")
      })

      println("--- <QUERY> ---")
      println(stringStatements.map(s ⇒ s"statement ->\n$s\n").mkString("\n"))
      println("--- </QUERY> ---")

      val responseF = connection.txCommit(stringStatements: _*)
      responseF.map(response ⇒ {
        CypherResultAndErrorParser.parseResponse(response, stringStatements)
      })
    }

    def apply2(queryBuilder: (QueryContext) ⇒ Unit)(implicit connection: Neo4jRestConnection, ec: ExecutionContext) = {
      val context = new QueryContext
      queryBuilder(context)

      val stringStatements = context.getStatements.map(statement ⇒ {
        statement.statementBuilder(statement)
        statement.getClauses.map(_.toString).mkString("\n")
      })

      println("--- <QUERY> ---")
      println(stringStatements.map(s ⇒ s"statement ->\n$s\n").mkString("\n"))
      println("--- </QUERY> ---")

      val responseF = connection.txCommit(stringStatements: _*)
      responseF.map(response ⇒ {
        CypherErrorOnlyParser.parseResponse(response, reads = None)
      })
    }
  }

  def statement(statementBuilder: (Statement) ⇒ Unit)(implicit context: QueryContext): Unit = {
    val statement = Statement(statementBuilder, context)
    context.addStatement(statement)
  }
}

