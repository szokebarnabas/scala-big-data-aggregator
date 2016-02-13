package aggregator

import org.scalatest.FunSuite

class AggregatorUTest extends FunSuite {

  implicit val rates: ExchangeRates = Map(
    ("HUF", "GBP") -> 0.0025,
    ("GBP", "HUF") -> 399.37
  )

  val source: Iterable[String] = List(
    "Defence ltd.,HUF,100.5",
    "Defence ltd.,GBP,50.5",
    "Plumber ltd.,HUF,515.060"
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
