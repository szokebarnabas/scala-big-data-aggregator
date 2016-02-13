package aggregator

object Aggregator {

  private def exchange(money: Money, toCurrency: Currency, rates: ExchangeRates) = toCurrency match {
    case toCurr if toCurr == money.currency => money
    case _ =>
      val currencyPair: (String, String) = (money.currency, toCurrency)
      val rate = rates.get(currencyPair) match {
        case Some(rate) => rate
        case None => throw new RuntimeException(s"Exchange rate not found $currencyPair")
      }
      money.exchange(toCurrency, rate)
  }

  def aggregatedByPartner(source: Iterable[String], currency: Currency)(implicit rates: ExchangeRates): Map[Partner, Money] = {
    source
      .map(Transaction.apply)
      .groupBy(_.partner)
      .map(x => (x._1, x._2.foldLeft(Money(0, currency))((sum, t) => sum + exchange(t.money, currency, rates))))
  }

  def aggregateOfPartner(source: Iterable[String], partner: Partner, currency: Currency)(implicit rates: ExchangeRates): Money = {
    source
      .map(Transaction.apply)
      .filter(p => p.partner == partner)
      .foldLeft(Money(0, currency))((sum, t) => sum + exchange(t.money, currency, rates))
  }
}
