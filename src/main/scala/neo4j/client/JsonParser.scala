package neo4j.client

import play.api.data.validation.ValidationError
import play.api.libs.json.{ Reads, JsError, JsPath, JsValue }
import play.api.libs.ws.WSResponse

import scala.util.{ Success, Failure, Try }

/** Parser abstraction to  used to parse JSON format of HttpResult content. To use this base class implementation of
 *  a `reads` method needs to be provided.
 */
trait JsonParser {

  /** Implementation of of converter from JsValue to target type.
   *  @return Returns converted value.
   */
  // FIXME why is this cypherQuery expecting Seq[String] why cant it be String*
  protected def parseJson[R](js: JsValue, cypherQuery: Seq[String])(implicit parser: Reads[R]): Try[Seq[R]]

  private def singleErrorMessage(error: (JsPath, scala.Seq[ValidationError])) = {
    val (path: JsPath, errors: Seq[ValidationError]) = error
    val message = errors.foldLeft(errors.head.message)((acc, err) ⇒ s"$acc,${err.message}")
    s"Errors at $path: $message"
  }

  protected def buildErrorMessage(errors: Seq[JsError]) = {
    errors.map(error ⇒ error.errors.tail.foldLeft(singleErrorMessage(error.errors.head))((acc, err) ⇒ s"acc,${singleErrorMessage(err)}"))
  }

  def parseResponse[R](response: WSResponse, cypherQuery: Seq[String])(implicit reads: Reads[R]): Try[Seq[R]] = {
    // TODO: dont use hardcoded 200
    if (response.status == 200) {
      parseJson[R](response.json, cypherQuery)
    } else {
      Failure(new InvalidResponseException(s"Response status <${response.status}> is not valid: ${response.json} "))
    }
  }

  /** Wrapper around parseResult[R] that does not case about the results and
   *  only cares about whether an Exception (error) was thrown or not.
   *
   *  @param response
   *  @return
   */
  def parseResponse(response: WSResponse, reads: Option[Reads[_]] = None): Try[Unit] = {
    // Don't have a better way of doing this for now, hence using [Int]
    parseResponse[Int](response, null) match {
      case Success(_)     ⇒ Success()
      case f @ Failure(e) ⇒ Failure(e)
    }
  }

}
