package aggregator

object Aggregator {

//  def aggregatedByPartner(source: Source, currency: Currency)(implicit rates: ExchangeRates) : Map[Partner, Money] = {
//    val op: (Money, Transaction) => Money = (sum, transaction) => sum + transaction.money.exchange(currency, rates((transaction.money.currency, currency)))
//    source.getLines()
//      .map(Transaction.apply)
//      .toTraversable
//      .groupBy(_.partner).map(x => (x._1, x._2.foldLeft(Money(0, currency))(op)))
//  }

  def aggregatedByPartner[A <: String](source : Iterable[A], currency: Currency)(implicit rates: ExchangeRates) : Map[Partner, Money] = {

    def sum(sum: Money, t: Transaction) = {
      val currencyPair: (String, String) = (t.money.currency, currency)

      val r = rates.get(currencyPair) match {
        case Some(rate) => rate
        case None => throw new RuntimeException(s"Exchange rate not found $currencyPair")
      }
      sum + t.money.exchange(currency, r)
    }

    source
      .map(Transaction.apply)
      .groupBy(_.partner)
      .map(x => (x._1, x._2.foldLeft(Money(0, currency))(sum)))
  }
}
