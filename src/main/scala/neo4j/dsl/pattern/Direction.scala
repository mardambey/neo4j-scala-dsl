package neo4j.dsl.pattern

/** Represents a direction in a Pattern.
 */
sealed trait Direction {
  def symbol: String

  override def toString: String = symbol
}

object NodeDirection {

  object Left extends Direction {
    override def symbol: String = "<--"
  }

  object Right extends Direction {
    override def symbol: String = "-->"
  }

  object Both extends Direction {
    override def symbol: String = "--"
  }

  object Start extends Direction {
    override def symbol: String = ""
  }

}

object RelationDirection {

  object Left extends Direction {
    override def symbol: String = "<-"
  }

  object Right extends Direction {
    override def symbol: String = "->"
  }

  object Both extends Direction {
    override def symbol: String = "-"
  }

  object Start extends Direction {
    override def symbol: String = ""
  }

}
