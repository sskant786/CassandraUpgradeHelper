package models

import java.time.LocalDateTime
import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import com.datastax.driver.core.{Row, Session}
import utils.Logging

import scala.concurrent.Future
import scala.util.Random

/**
 * Created by Suryakant on 17-04-2020.
 */
case class Employee(id: String,
                    name: String,
                    designation: String,
                    salary: Float,
                    boardedAt: Date)

//pass cassandra session to it
class EmployeeManager(session: Session) extends Logging{

  import utils.Java2Scala.asScala

  private val keyspace = "test"
  private val table = "employee"

  private val insertPS = session.prepare(s"INSERT INTO $keyspace.$table (id,boardedat,designation,name,salary) VALUES (?, ?, ?, ?, ?);")
  private val fetchPS = session.prepare(s"SELECT * FROM $keyspace.$table LIMIT 1")
  private val updatePS = session.prepare(s"UPDATE $keyspace.$table SET salary = ? WHERE id = ?")
  private val deletePS = session.prepare(s"DELETE from $keyspace.$table WHERE id = ?")

  private def toEmployee(r: Row): Employee = Employee(
    r.getString("id"),
    r.getString("name"),
    r.getString("designation"),
    r.getFloat("salary"),
    r.getTimestamp("boardedat")
  )

  private def onboardEmployee: Employee = {
    val empId = s"EMP${Random.alphanumeric.take(5).mkString}"
    Employee(empId, "S S Kant","SE", 6000, new Date())
  }

  def add(): Future[Boolean] = {
    val employee = onboardEmployee
    val boundStatement = insertPS.bind(
      employee.id,
      employee.boardedAt,
      employee.designation,
      employee.name,
      employee.salary.asInstanceOf[java.lang.Float]
    )
    session.executeAsync(boundStatement).map(_ => true)
      .recover {
      case ex: Throwable =>
        logger.error(s"Error while inserting on boarding employee ${employee}", ex)
        false
    }
  }

  def delete(empId: String) = {
    session.executeAsync(deletePS.bind(empId)).map{ _ =>
      logger.info(s"Employee with id [$empId] got fired")
    }.recover{
      case th: Throwable =>
        logger.error(s"Error occurred while deleting employee $empId", th)
    }
  }

  def update(emp: Employee) = {
    val updatedSalary = emp.salary + 100
    session.executeAsync(updatePS.bind(updatedSalary.asInstanceOf[java.lang.Float], emp.id)).map{_ =>
      logger.info(s"Employee salary update from ${emp.salary} to $updatedSalary")
    }.recover{
      case th: Throwable =>
        logger.error(s"Error while updating employee [$emp] ", th)
    }
  }

  def get(): Future[Option[Employee]] = {
    session.executeAsync(fetchPS.bind()).map { rows =>
      rows.headOption.map(toEmployee)
    }
  }

}
