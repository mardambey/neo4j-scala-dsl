package neo4j.client

import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.util.{ Success, Failure, Try }

/*

TODO
Example of Js result with error
--------------------------------
{
  "results": [
    {
      "columns": [],
      "data": []
    }
  ],
  "errors": [
    {
      "code": "Neo.ClientError.Statement.InvalidSyntax",
      "message": "Invalid input 'p': expected whitespace, comment, node labels, MapLiteral, a parameter, a relationship pattern, '(', '.', '[', \"=~\", IN, IS, '^', '*', '\/', '%', '+', '-', '<', '>', \"<=\", \">=\", '=', \"<>\", \"!=\", AND, XOR, OR, ',', LOAD CSV, START, MATCH, UNWIND, MERGE, CREATE, SET, DELETE, REMOVE, FOREACH, WITH, RETURN, UNION, ';' or end of input (line 4, column 21 (offset: 224))\n\"DELETE postToRETURN post\"\n                     ^"
    }
  ]
}

*/
trait CypherResponseParser extends JsonParser {

  @throws[Exception]
  @throws[JsonValidationException]
  def parseResult[R](result: JsValue)(implicit parser: Reads[R]): Try[Seq[R]]

  @throws[Exception]
  @throws[JsonValidationException] // FIXME why Seq[String] not String*
  def parseErrors[R](js: JsValue, cypherQuery: Seq[String]): Failure[Seq[R]]

  override protected def parseJson[R](js: JsValue, cypherQuery: Seq[String])(implicit parser: Reads[R]): Try[Seq[R]] = {
    (js \ "errors").as[JsArray].value.toList match {
      case errors :: Nil ⇒
        parseErrors[R](js, cypherQuery)

      case _ ⇒
        parseResult[R]((js \ "results").as[JsArray].value.head)
    }
  }
}

trait CypherNoopResultParser extends CypherResponseParser {
  override def parseResult[R](result: JsValue)(implicit parser: Reads[R]): Try[Seq[R]] = Try(Seq.empty[R])
}

trait CypherErrorParser extends CypherResponseParser {

  implicit val readError: Reads[ErrorMessage] = {
    (
      (__ \ "code").read[String] and (__ \ "message").read[String])(ErrorMessage.apply _)
  }

  override def parseErrors[R](js: JsValue, cypherQuery: Seq[String]): Failure[Seq[R]] = {
    val errorReads = (__ \ "errors").read[Seq[ErrorMessage]]
    val error = errorReads.reads(js)
    error match {
      case JsSuccess(e, _) ⇒ Failure(new Exception(s"CypherQuery '$cypherQuery' failed: ${e.toString()}"))
      case e: JsError ⇒ Failure(new JsonValidationException(s"CypherQuery '$cypherQuery' failed:" +
        buildErrorMessage(Seq(e)).mkString("\n"), Some(js)))
    }
  }
}

/** Base class implementing skeleton for paring the result.
 *
 *  ```
 *  {
 *   "results":[{
 *     "columns":["a","b"],
 *     "data":[
 *       {
 *         "row":[{"name":"Michael"},{"something"}]
 *       }
 *     ]
 *   }],
 *   "errors":[]
 *  }
 *  ```
 *
 */
trait CypherResultParser extends CypherResponseParser {

  override def parseResult[R](result: JsValue)(implicit parser: Reads[R]): Try[Seq[R]] = {
    val rows = (result \ "data").as[JsArray].value.map(row ⇒ {

      // TODO: there should be a cleaner way of doing this; perhaps by looking at the returned "columns" field.
      val r = row \ "row"
      if (r.apply(1).toOption.isDefined) {
        r
      } else {
        r(0)
      }

    })

    val parsed = rows.map(row ⇒ row.validate[R])
    if (parsed.forall(res ⇒ res.isSuccess)) {
      Success(parsed.map(_.get))
    } else {
      val e = buildErrorMessage(parsed.filter(_.isError).asInstanceOf[Seq[JsError]])
      Failure(new JsonValidationException(e.mkString("\n"), Some(result)))
    }
  }
}

object CypherResultAndErrorParser extends CypherResultParser with CypherErrorParser

object CypherErrorOnlyParser extends CypherNoopResultParser with CypherErrorParser

