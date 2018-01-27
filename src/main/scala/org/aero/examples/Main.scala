package org.aero.examples

import scala.concurrent.duration._
import org.aero.AeroOps._
import org.aero._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {
    val host = "localhost"
    val port = 3000

    implicit val schema = Schema("test", "org-os")
    implicit val ac = AeroClient(host, port)

    val eventualUnit = for {
      _ <- put("002", (WriteBin("key", "002"), WriteBin("string_column", "string"), WriteBin("double_column", 1.1)))
      (key, value) <- get("002", ("key".as[String], "double_column".as[Double]))
      optionResult <- getOpt("002", "string_column".as[String])
    } yield {
      println(s"$key = $value") //  print 002 = 1.1
      println(optionResult)     //  print Some(String)
    }

    Await.ready(eventualUnit, Duration.Inf)

    ac.close()
  }
}
