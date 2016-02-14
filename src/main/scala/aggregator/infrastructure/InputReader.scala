package aggregator.infrastructure

import scala.io.Source


trait InputReader[T <: Iterator[_]] {
  def stream[U](f: String => U): Stream[U]
}

class FileInputReader(input: Source) extends InputReader[Source] {

  override def stream[U](f: (String) => U): Stream[U] = input.getLines().map(f).toStream
}
