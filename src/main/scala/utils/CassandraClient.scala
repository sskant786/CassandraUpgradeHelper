package utils

import com.datastax.driver.core.{Cluster, ProtocolVersion}
import com.datastax.driver.core.policies.ConstantReconnectionPolicy

/**
 * Created by Suryakant on 17-04-2020.
 */

object CassandraClient {

  private val logger = LogUtils.getLogger
  private val nodes = ConfigReader.getStringList("CASSANDRA_ENDPOINT").map(_.toList).getOrElse(List("127.0.0.1"))
  private val protocolVersion = ConfigReader.getString("CASSANDRA_CONNECTION_PROTOCOL_VERSION").getOrElse("V3")
  logger.info(s"using CASSANDRA_CONNECTION_PROTOCOL_VERSION = $protocolVersion")

  private val cassandraConnectionProtocol = protocolVersion.trim.toUpperCase match {
    case "V1" => ProtocolVersion.V1
    case "V2" => ProtocolVersion.V2
    case "V3" => ProtocolVersion.V3
    case "V4" => ProtocolVersion.V4
    case "V5" => ProtocolVersion.V5
    case _ => logger.warn(s"Protocol version [$protocolVersion] mentioned in conf is not a valid version. So using default version i.e V3")
      ProtocolVersion.V3
  }

  private val cluster = Cluster.builder
    .addContactPoints(nodes: _*).withReconnectionPolicy(new ConstantReconnectionPolicy(1000L))
    .withProtocolVersion(cassandraConnectionProtocol)
    .build

  val session = cluster.connect()

  logger.info(s"Protocol version used by cluster is: ${cluster.getConfiguration.getProtocolOptions.getProtocolVersion}")
}
