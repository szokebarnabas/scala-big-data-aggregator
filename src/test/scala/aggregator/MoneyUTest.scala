package aggregator

import org.scalatest.FunSuite

class MoneyUTest extends FunSuite {

  val HUF_TO_GBP_RATE = 0.0025
  val GBP_TO_HUF_RATE = 397.87

  test("Converts 1 GBP to HUF") {
    val gbp = Money(1, new Currency("GBP"))
    val result = gbp.exchange(new Currency("HUF"), GBP_TO_HUF_RATE)
    assert(result == Money(397.87, "HUF"))
  }

  test("Converts 1 HUF to GBP") {
    val gbp = Money(1, new Currency("HUF"))
    val result = gbp.exchange(new Currency("GBP"), HUF_TO_GBP_RATE)
    assert(result == Money(0.0025, "GBP"))
  }

  test("Converts 1 GBP to GBP") {
    val gbp = Money(1, new Currency("GBP"))
    val result = gbp.exchange(new Currency("GBP"), 0)
    assert(result == Money(1, "GBP"))
  }

  test("Successfully sum money objects with the same currency") {
    assert(Money(20.5, "GBP") + Money(30.3, "GBP") == Money(50.8, "GBP"))
  }

  test("Cannot sum money objects with different currency") {
    intercept[RuntimeException] {
      Money(20.5, "GBP") + Money(30.3, "HUF")
    }
  }

}
