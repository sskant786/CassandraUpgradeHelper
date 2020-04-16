package utils

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.policies.ConstantReconnectionPolicy

/**
 * Created by Suryakant on 17-04-2020.
 */

object CassandraClient {

  private val nodes = ConfigReader.getStringList("CASSANDRA_ENDPOINT").map(_.toList).getOrElse(List("127.0.0.1"))

  private val cluster = Cluster.builder.addContactPoints(nodes: _*).withReconnectionPolicy(new ConstantReconnectionPolicy(1000L)).build

  val session = cluster.connect()
}
