package org.excavator.rsocket.rpc

import io.rsocket.RSocketFactory
import io.rsocket.transport.netty.client.TcpClientTransport
import org.excavator.grpc.rsocket.rpc.SimpleServiceClient
import org.slf4j.LoggerFactory

class RsocketRpcClientApplication {

  val logger = LoggerFactory.getLogger(classOf[RsocketRpcClientApplication])

  var serviceClient: SimpleServiceClient = null
  val requests = RsocketRpcHelper.buildRequests()
  val simpleRequest = RsocketRpcHelper.buildRequest()

  def connect(port: Int) = {
    val rSocket = RSocketFactory.connect()
      .transport(TcpClientTransport.create(port))
      .start()
      .block()
    serviceClient = new SimpleServiceClient(rSocket)
  }

  def streamingRequestSingleResponse() = {
    serviceClient.streamingRequestSingleResponse(requests, io.netty.buffer.Unpooled.EMPTY_BUFFER)
  }

  def streamingRequestAndResponse() = {
    serviceClient.streamingRequestAndResponse(requests, io.netty.buffer.Unpooled.EMPTY_BUFFER)
  }

  def requestStream() = {
    serviceClient.requestStream(simpleRequest)
  }

  def fireAndForget() = {
    serviceClient.fireAndForget(simpleRequest)
  }

  def requestReply() = {
    serviceClient.requestReply(simpleRequest)
  }
}
