package utils

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success, Try}
import scala.collection.JavaConverters._

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
