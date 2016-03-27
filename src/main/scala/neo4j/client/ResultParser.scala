package neo4j.client

import play.api.libs.ws.WSResponse

import scala.util.Try

/** Result parser is used to parse REST response object to a meaningful business object.
 */
trait ResultParser {

  /** Parse the HttpResponse object to a business object. In case of response status being invalid or response data
   *  corrupted Left with corresponding message should be returned. Otherwise the function should return Right
   *
   *  @param response HttpResponse object.
   *  @tparam R type of resulting object.
   *  @return Result of parsing.
   */
  def parseResult[R](response: WSResponse): Try[R]
}
