package org.excavator.rsocket.rpc.test

import org.excavator.rsocket.rpc.{RsocketRpcClientApplication, RsocketRpcServerApplication}
import org.junit.jupiter.api._
import org.slf4j.LoggerFactory

class RsocketRpcTest {
  val logger = LoggerFactory.getLogger(classOf[RsocketRpcTest])

  @Test
  @DisplayName("testRsocketRpc")
  @RepeatedTest(1000)
  def testRsocketRpc() = {
    val server = new RsocketRpcServerApplication
    server.start(53000)
    val client = new RsocketRpcClientApplication()
    client.connect(53000)
  }
}

object RsocketRpcTest {

  @BeforeAll
  def initServer() = {
    val host = "localhost"
    val port = 53000
  }

}
