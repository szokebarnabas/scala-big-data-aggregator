package aggregator

import aggregator.domain.Aggregator
import aggregator.domain.Model.{Money, Transaction, ExchangeRate, Currency}
import org.scalatest.FunSuite

class AggregatorUTest extends FunSuite {

  implicit val rates: Stream[ExchangeRate] = Stream(
    ExchangeRate("HUF", "GBP", 0.0025),
    ExchangeRate("GBP", "HUF", 399.37)
  )

  val source: Stream[Transaction] = Stream(
    Transaction("Defence ltd.", Money(100.5, "HUF")),
    Transaction("Defence ltd.", Money(50.5, "GBP")),
    Transaction("Plumber ltd.", Money(515.060, "HUF"))
  )

  test("Returns the sum of the transactions per partner") {
    val currency = new Currency("GBP")
    def result = Aggregator.aggregatedByPartner(source, currency)

    assert(result("Defence ltd.").amount == 50.75125)
    assert(result("Plumber ltd.").amount == 1.28765)
  }


  test("Returns the sum of the transactions of a partner") {
    val currency = new Currency("GBP")
    val partner = "Defence ltd."

    def result = Aggregator.aggregateOfPartner(source, partner, currency)

    assert(result.amount == 50.75125)
  }

  test("Throws exception when an exchange rate is not found") {
    val currency = new Currency("EUR")
    val partner = "Defence ltd."

    intercept[RuntimeException] {
      Aggregator.aggregateOfPartner(source, partner, currency)
    }

  }
}
