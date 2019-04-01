package org.excavator.rsocket.rpc

import io.rsocket.{RSocket, RSocketFactory}
import io.rsocket.transport.netty.client.TcpClientTransport
import org.excavator.grpc.rsocket.rpc.{SimpleRequest, SimpleServiceClient}
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux

class RsocketRpcClientApplication {

  val logger = LoggerFactory.getLogger(classOf[RsocketRpcClientApplication])

  var serviceClient: SimpleServiceClient = null
  val requests = RsocketRpcHelper.buildRequests()

  def connect(port: Int) = {
    val rSocket = RSocketFactory.connect()
      .transport(TcpClientTransport.create(port))
      .start()
      .block()
    serviceClient = new SimpleServiceClient(rSocket)
  }

  def streamingRequestSingleResponse() = {

    val response = serviceClient.streamingRequestSingleResponse(requests, io.netty.buffer.Unpooled.EMPTY_BUFFER)

    response
  }

  def streamingRequestAndResponse() = {
    val responseFlux = serviceClient.streamingRequestAndResponse(requests, io.netty.buffer.Unpooled.EMPTY_BUFFER)
    responseFlux
  }
}
