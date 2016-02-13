

package object aggregator {

  type Currency = String
  type Partner = String
  type ExchangeRates = Map[(String, String), Double]

  case class ExchangeRate(fromCurrency: Currency, toCurrency : Currency, rate: Double)

  case class Money(amount: Double, currency: Currency) {
    def +(money: Money): Money = money match {
      case Money(newAmount, newCurrency) if newCurrency == currency => Money(amount + newAmount, currency)
    }

    def exchange(toCurrency: Currency, rate : Double) = toCurrency match {
      case curr: String if curr == currency => this
      case _ => new Money(amount * rate, toCurrency)
    }
  }

  case class Transaction(partner: Partner, money: Money)

  object Transaction {
    def apply(line: String): Transaction = {
      val tokens = line.split(",")
      Transaction(tokens(0), Money(tokens(2).toDouble, tokens(1)))
    }
  }
}