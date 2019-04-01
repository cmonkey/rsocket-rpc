package org.excavator.rsocket.rpc

import io.rsocket.{RSocket, RSocketFactory}
import io.rsocket.transport.netty.client.TcpClientTransport
import org.excavator.grpc.rsocket.rpc.{SimpleRequest, SimpleServiceClient}
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux

class RsocketRpcClientApplication {

  val logger = LoggerFactory.getLogger(classOf[RsocketRpcClientApplication])

  var rSocket: RSocket = null

  def connect(port: Int) = {
    rSocket = RSocketFactory.connect()
      .transport(TcpClientTransport.create(port))
      .start()
      .block()
  }

  def streamingRequestSingleResponse() = {

    val serviceClient = new SimpleServiceClient(rSocket)

    val requests:Flux[SimpleRequest] = RsocketRpcHelper.buildRequests()

    val response = serviceClient.streamingRequestSingleResponse(requests, io.netty.buffer.Unpooled.EMPTY_BUFFER).block

    response
  }
}
