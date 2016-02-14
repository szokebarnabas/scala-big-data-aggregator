package aggregator

import aggregator.domain.Aggregator
import aggregator.domain.Model._
import aggregator.infrastructure.FileInputReader

import scala.io.Source

object Main extends App {

  val source = Source.fromInputStream(getClass.getResourceAsStream("/transactions.csv"))
  val rates = Source.fromInputStream(getClass.getResourceAsStream("/exchangerates.csv"))

  val transactionStream = new FileInputReader(source).stream(Transaction.apply)
  implicit val exchangeRateStream = new FileInputReader(rates).stream(ExchangeRate.apply)

  val byPartner = Aggregator.aggregatedByPartner(transactionStream, new Currency("GBP"))
  val ofPartner = Aggregator.aggregateOfPartner(transactionStream, new Partner("Plumber ltd."), new Currency("GBP"))

  println(s"By partner: $byPartner")
  println(s"Of partner Plumber ltd.: $ofPartner")
}

