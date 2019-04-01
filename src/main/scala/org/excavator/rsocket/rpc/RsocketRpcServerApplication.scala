package org.excavator.rsocket.rpc

import java.util.Optional

import io.rsocket.RSocketFactory
import io.rsocket.rpc.rsocket.RequestHandlingRSocket
import io.rsocket.transport.netty.server.TcpServerTransport
import org.excavator.grpc.rsocket.rpc.SimpleServiceServer
import reactor.core.publisher.Mono

class RsocketRpcServerApplication {

  def start(port: Int) = {
    val serviceServer = new SimpleServiceServer(new DefaultSimpleService(), Optional.empty(), Optional.empty())

    val closeableChannel = RSocketFactory
      .receive()
      .acceptor((setup,sendingSocket) => {
        Mono.just(new RequestHandlingRSocket(serviceServer))
      })
      .transport(TcpServerTransport.create(port))
      .start()
      .log("start")
      .block()
  }
}
