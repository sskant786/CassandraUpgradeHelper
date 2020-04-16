package models

import java.time.LocalDateTime

import utils.Logging

/**
 * Created by Suryakant on 17-04-2020.
 */
case class Employee(id: String,
                    name: String,
                    designation: String,
                    salary: Float,
                    boardedAt: LocalDateTime)

//pass cassandra session to it
class EmployeeManager() extends Logging{
  def add() = {
    logger.info(s"Employee added")
  }

  def delete() = {
    logger.info(s"Employee deleted")
  }

  def get() = {
    logger.info(s"Employee deleted")
  }
}
