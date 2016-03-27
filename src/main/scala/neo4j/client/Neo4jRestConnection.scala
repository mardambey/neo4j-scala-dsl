package neo4j.client

import com.typesafe.config.Config
import play.api.libs.json._
import play.api.libs.ws.{ WSAuthScheme, WSRequest, WSResponse }

import scala.concurrent.Future

trait Neo4jRestConnection {

  protected val config: Config
  protected val client = WSClient.client

  val SERVER_ROOT_HOST = config.getString("neo4j-scala-dsl.neo4j.rest.host")
  val SERVER_ROOT_PORT = config.getInt("neo4j-scala-dsl.neo4j.rest.port")
  val SERVER_ROOT_URI = s"http://$SERVER_ROOT_HOST:$SERVER_ROOT_PORT"
  val SERVER_ROOT_DATA_URI = s"$SERVER_ROOT_URI/db/data"
  val SERVER_USERNAME = config.getString("neo4j-scala-dsl.neo4j.rest.username")
  val SERVER_PASSWORD = config.getString("neo4j-scala-dsl.neo4j.rest.password")

  val txUri = s"$SERVER_ROOT_DATA_URI/transaction/commit"

  def payloadJson(statements: String*): JsObject =
    Json.obj("statements" -> statements.map(s ⇒ Map("statement" -> s)).toList)

  def request(url: String): WSRequest = client.url(url)
    .withHeaders(
      ("Content-Type", "application/json"),
      ("Accept", "application/json"))
    .withAuth(SERVER_USERNAME, SERVER_PASSWORD, WSAuthScheme.BASIC)

  def txCommit(queries: String*): Future[WSResponse] = {
    println(s"txCommit: $queries")
    request(txUri).post(payloadJson(queries: _*))
  }
}

