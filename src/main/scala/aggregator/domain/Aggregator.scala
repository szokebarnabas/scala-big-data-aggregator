package aggregator.domain

import aggregator.domain.Model._

object Aggregator {

  private def exchange(money: Money, toCurrency: Currency, rates: Stream[ExchangeRate]) = toCurrency match {
    case toCurr if toCurr == money.currency => money
    case _ =>
      val rate = rates.find(rate => rate match {
        case ExchangeRate(from, to, _) if from == money.currency && to == toCurrency => true
        case _ => false
      }) match {
        case Some(r) => r.rate
        case None => throw new RuntimeException(s"Exchange rate not found ${money.currency} -> $toCurrency")
      }
      money.exchange(toCurrency, rate)
  }

  private def sum(currency: Currency, rates: Stream[ExchangeRate]): (Money, Transaction) => Money = (sum, t) => sum + exchange(t.money, currency, rates)

  def aggregatedByPartner(source: Stream[Transaction], currency: Currency)(implicit rates: Stream[ExchangeRate]): Map[Partner, Money] = {
    source.groupBy(_.partner).map(x => (x._1, x._2.foldLeft(Money(0, currency))(sum(currency, rates))))
  }


  def aggregateOfPartner(source: Stream[Transaction], partner: Partner, currency: Currency)(implicit rates: Stream[ExchangeRate]): Money = {
    source.filter(p => p.partner == partner).foldLeft(Money(0, currency))((sum(currency, rates)))
  }
}
