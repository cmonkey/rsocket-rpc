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
    val response = RsocketRpcTest.client.streamingRequestSingleResponse()
    logger.info(s"response = ${response}")
  }
}

object RsocketRpcTest {

  val port = 53000

  var client:RsocketRpcClientApplication = null

  @BeforeAll
  def initServer() = {
    val server = new RsocketRpcServerApplication
    server.start(port)

    client = new RsocketRpcClientApplication
    client.connect(port)
  }

}
