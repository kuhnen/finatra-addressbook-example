package com.github.kuhnen

import com.twitter.util.{Future => TwitterF}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfter, FlatSpecLike, ShouldMatchers}

import scala.concurrent.{Future => ScalaF, Promise => ScalaP}


/**
 * Created by kuhnen on 1/13/15.
 */

trait CommonTraits extends FlatSpecLike with ShouldMatchers with MockFactory with ScalaFutures with BeforeAndAfter

object FutureHelper {

  implicit def twitterFutureToScalaFuture[T](twitterF: TwitterF[T]): ScalaF[T] = {
    val scalaP = ScalaP[T]
    twitterF.onSuccess { r: T =>
      scalaP.success(r)
    }
    twitterF.onFailure { e: Throwable =>
      scalaP.failure(e)
    }
    scalaP.future
  }

}

