package aggregator

import scala.collection.immutable.HashMap
import scala.io.Source

object Main extends App {

  val source = Source.fromInputStream(getClass.getResourceAsStream("/transactions.csv"))
  val rates = Source.fromInputStream(getClass.getResourceAsStream("/exchangerates.csv"))

  def tokens(line : String) : (String, String, Double) = {
    val tokens = line.split(",")
    (tokens(0), tokens(1), tokens(2).toDouble)
  }

  implicit val exchangeRates = rates.getLines().map(tokens).foldLeft(new HashMap[(String, String), Double])((map, tuple) => map + ((tuple._1, tuple._2) -> tuple._3))

  val byPartner = Aggregator.aggregatedByPartner(source.getLines().toIterable, new Currency("GBP"))

  println(byPartner)
}

