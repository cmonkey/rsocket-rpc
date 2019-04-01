package org.excavator.rsocket.rpc.test

import org.excavator.rsocket.rpc.{RsocketRpcClientApplication, RsocketRpcServerApplication}
import org.junit.jupiter.api._
import org.junit.jupiter.api.Assertions._
import org.slf4j.LoggerFactory

class RsocketRpcTest{
  val logger = LoggerFactory.getLogger(classOf[RsocketRpcTest])

  @Test
  @DisplayName("testStreamingRequestSingleResponse")
  @RepeatedTest(10)
  def testStreamingRequestSingleResponse() = {
    val responseMono = RsocketRpcTest.client.streamingRequestSingleResponse()
    responseMono.subscribe(simpleResponse => {
      logger.info(s"response = ${simpleResponse.getResponseMessage}")
    })

    assertNotNull(responseMono)
  }

  @Test
  @DisplayName("testStreamingRequestAndResponse")
  @RepeatedTest(10)
  def testStreamingRequestAndResponse() = {
    val responseFlux = RsocketRpcTest.client.streamingRequestAndResponse()
    responseFlux.subscribe((simpleResponse) => {logger.info(s"response = ${simpleResponse}")})
    assertNotNull(responseFlux)
  }

  @Test
  @DisplayName("testRequestStream")
  @RepeatedTest(10)
  def testRequestStream() = {
    val responseFlux = RsocketRpcTest.client.requestStream()
    responseFlux.subscribe((simpleResponse) => {logger.info(s"response = ${simpleResponse}")})
    assertNotNull(responseFlux)
  }

  @Test
  @DisplayName("testFireAndForget")
  @RepeatedTest(10)
  def testFireAndForget() = {
    val responseMono = RsocketRpcTest.client.fireAndForget()

    responseMono.subscribe(e => logger.info(s"response = ${e}"))

    assertNotNull(responseMono)
  }

  @Test
  @DisplayName("testRequestReply")
  @RepeatedTest(10)
  def testRequestReply() = {
    val responseMono = RsocketRpcTest.client.requestReply()
    responseMono.subscribe(response => logger.info(s"response = ${response}"))
    assertNotNull(responseMono)
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
