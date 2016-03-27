package neo4j.client

import play.api.libs.ws.ning._

object WSClient {

  implicit val client = NingWSClient()

  def shutdown() = client.close()

}

