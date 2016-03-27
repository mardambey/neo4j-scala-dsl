package neo4j.dsl

import neo4j.dsl.graph.DefaultFormatters

trait DefaultImports {
  import DefaultFormatters._
  implicit val stringFormatter = new StringFormatter
  implicit val intFormatter = new IntegerFormatter
  implicit val doubleFormatter = new DoubleFormatter
  implicit val booleanFormatter = new BooleanFormatter
  implicit val listStringFormatter = new ListStringFormatter
  implicit val mapStringFormatter = new MapStringFormatter
}
