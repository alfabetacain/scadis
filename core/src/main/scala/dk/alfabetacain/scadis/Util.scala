package dk.alfabetacain.scadis

import cats.MonadThrow
import cats.syntax.all._
import dk.alfabetacain.scadis.parser.Value

import java.nio.charset.StandardCharsets

private[scadis] object Util {

  private[scadis] def expect[F[_]: MonadThrow, A](action: F[Value], mapper: PartialFunction[Value, A]): F[A] = {
    action.flatMap { result =>
      if (mapper.isDefinedAt(result)) {
        mapper(result).pure[F]
      } else {
        MonadThrow[F].raiseError(new IllegalArgumentException(s"Unexpected unparsable response: $result"))
      }
    }
  }

  private[scadis] def toBS(input: String): Value.RBulkString = {
    Value.RBulkString(input.getBytes(StandardCharsets.US_ASCII))
  }
}
