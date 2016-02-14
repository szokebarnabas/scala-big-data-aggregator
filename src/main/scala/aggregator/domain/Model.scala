package aggregator.domain

object Model {

  type Currency = String
  type Partner = String

  case class ExchangeRate(fromCurrency: Currency, toCurrency : Currency, rate: Double)

  object ExchangeRate {
    def apply(line : String): ExchangeRate = {
      val tokens = line.split(",")
      ExchangeRate(tokens(0), tokens(1), tokens(2).toDouble)
    }
  }

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