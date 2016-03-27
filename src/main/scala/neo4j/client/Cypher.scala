package neo4j.client

import play.api.libs.json.Reads

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

object Cypher {

  // TODO send original query back to parseResponse to print out in case of an error
  def apply[R](query: String*)(implicit connection: Neo4jRestConnection, reads: Reads[R], ec: ExecutionContext): Future[Try[Seq[R]]] = {
    val responseF = connection.txCommit(query: _*)
    responseF.map(response ⇒ {
      CypherResultAndErrorParser.parseResponse(response, query)
    })
  }

  // Version of the above that is not interested in parsing the response, only returns errors on failure
  def apply2(query: String)(implicit connection: Neo4jRestConnection, ec: ExecutionContext) = {
    val responseF = connection.txCommit(query)
    responseF.map(response ⇒ {
      // This sucks, we have to do it for now to select the proper parseResponse() call.
      CypherErrorOnlyParser.parseResponse(response, reads = None)
    })
  }
}
