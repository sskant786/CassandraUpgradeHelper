import models.EmployeeManager
import utils.{CassandraClient, ConfigReader, Logging}

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Suryakant on 17-04-2020.
 */

object Start extends Logging{
  private val employeeManager = new EmployeeManager(CassandraClient.session)

  def main(args: Array[String]): Unit = {
    println("app started...")

    start()
  }

  /**
   * Run application until get terminated
   */
  private def start() = {
    val readPerMinute = ConfigReader.getLong("db.operation.read").getOrElse(10L)
    val writePerMinute = ConfigReader.getLong("db.operation.write").getOrElse(10L)

    logger.info(s"no of read per min: $readPerMinute, no of write per min: $writePerMinute")

    while(true) {
      doWrite(writePerMinute)
      doReadUpdateDelete(readPerMinute)
    }
  }

  private def doWrite(wpm: Long)={
    employeeManager.add().map {
      case true =>
        logger.info("new employee has been on boarded")
      case _ =>
        logger.warn(s"Failed to on board new employee")
    }
    Thread.sleep(delayForOperation(wpm))
  }

  private def doReadUpdateDelete(rpm: Long): Unit ={
    employeeManager.get().map{
      case Some(employee) =>
        logger.info(s"Got employee details [$employee]")
        employeeManager.update(employee)
        employeeManager.delete(employee.id)
      case None =>
        logger.warn(s"No entry for employee found in DB")
    }
    Thread.sleep(delayForOperation(rpm))
  }

  private def delayForOperation(noOfReq: Long): Long = {
    ((1000 * 60.0) / (noOfReq*2)).toLong
  }
}