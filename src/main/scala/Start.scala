import models.EmployeeManager
import utils.{ConfigReader, Logging}

/**
 * Created by Suryakant on 17-04-2020.
 */

object Start extends Logging{
  private val employeeManager = new EmployeeManager

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
      doRead(readPerMinute)
    }
  }

  private def doWrite(wpm: Long)={
    employeeManager.add()
    Thread.sleep(delayForOperation(wpm))
  }

  private def doRead(rpm: Long): Unit ={
    employeeManager.get()
    Thread.sleep(delayForOperation(rpm))
  }

  private def delayForOperation(noOfReq: Long): Long = {
    ((1000 * 60.0) / (noOfReq*2)).toLong
  }
}