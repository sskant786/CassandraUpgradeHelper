package utils

import java.util.concurrent.Executor

import com.google.common.util.concurrent.{FutureCallback, Futures, ListenableFuture}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success, Try}
import scala.collection.JavaConverters._
import scala.concurrent.{Future, Promise}

/**
 * Created by Suryakant on 17-04-2020.
 */

object LogUtils {
  private val logger = Logger("application")
  def getLogger = logger
}

trait Logging {
  val logger = LogUtils.getLogger
}

object Java2Scala {
  implicit def asScala[T](listenableFuture: ListenableFuture[T])(implicit ex: Executor): Future[T] = {
    val p = Promise[T]
    Futures.addCallback(listenableFuture, new FutureCallback[T]() {
      def onSuccess(result: T) = p success result
      def onFailure(t: Throwable) = p failure t
    }, ex)
    p future
  }
}

object ConfigReader extends Logging {
  private val config = ConfigFactory.load()

  def getSubConf(key: String): Config = Try(config.getConfig(key)) match {
    case Success(cnf) => cnf
    case Failure(ex) => logger.warn(s"[$key] no such configuration is found, returning parent config")
      config
  }

  def getString(key: String): Option[String] = Try(config.getString(key)).toOption

  def getInt(key: String): Option[Int] = Try(config.getInt(key)).toOption

  def getLong(key: String): Option[Long] = Try(config.getLong(key)).toOption

  def getStringList(key: String): Option[Seq[String]] = Try(config.getStringList(key)).toOption.map(_.asScala.toIndexedSeq)
}
