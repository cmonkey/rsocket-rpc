package org.excavator.rsocket.rpc.test

import org.junit.jupiter.api._
import org.slf4j.LoggerFactory

class RsocketRpcTest {
  val logger = LoggerFactory.getLogger(classOf[RsocketRpcTest])

  @Test
  @DisplayName("testProductByOK")
  @RepeatedTest(1000)
  def testProductByOK() = {
  }
}

object RsocketRpcTest {

  @BeforeAll
  def initServer() = {
    val host = "localhost"
    val port = 53000
  }

}
