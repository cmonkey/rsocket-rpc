package org.excavator.rsocket.rpc

import io.rsocket.RSocketFactory
import io.rsocket.transport.netty.client.TcpClientTransport
import org.excavator.grpc.rsocket.rpc.{SimpleRequest, SimpleServiceClient}
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux

class RsocketRpcClientApplication {

  val logger = LoggerFactory.getLogger(classOf[RsocketRpcClientApplication])

  def connect(port: Int) = {
    val rSocket = RSocketFactory.connect()
      .transport(TcpClientTransport.create(port))
      .start()
      .block()

    val serviceClient = new SimpleServiceClient(rSocket)

    val requests:Flux[SimpleRequest] = RsocketRpcHelper.buildRequests()

    val response = serviceClient.streamingRequestSingleResponse(requests, io.netty.buffer.Unpooled.EMPTY_BUFFER).block

    logger.info(s"response = ${response}")
  }
}
