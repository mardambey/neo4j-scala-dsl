package neo4j

import play.api.libs.json.JsValue

package object client {

  class JsonValidationException(msg: String, json: Option[JsValue] = None) extends Exception(msg + json.map(j ⇒ s" for JSON $j").getOrElse(""))

  class InvalidResponseException(msg: String) extends Exception(msg)

  case class ErrorMessage(code: String, msg: String)
}
