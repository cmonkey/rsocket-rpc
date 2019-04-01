package org.excavator.rsocket.rpc

import java.time.Duration

import com.google.protobuf.Empty
import io.netty.buffer.ByteBuf
import org.excavator.grpc.rsocket.rpc.{SimpleRequest, SimpleResponse, SimpleService}
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.{Flux, Mono}

class DefaultSimpleService extends SimpleService{
  val logger = LoggerFactory.getLogger(classOf[DefaultSimpleService])
  /**
    * <pre>
    * Request / Response
    * </pre>
    */
  override def requestReply(message: SimpleRequest, metadata: ByteBuf): Mono[SimpleResponse] = {
    Mono.fromCallable(() => {
      SimpleResponse.newBuilder()
        .setResponseMessage("we got the message -> " + message.getRequestMessage)
        .build()
    })
  }

  /**
    * <pre>
    * Fireand Forget
    * </pre>
    */
  override def fireAndForget(message: SimpleRequest, metadata: ByteBuf): Mono[Empty] = {
    logger.info("got message = {}", message.getRequestMessage)
    Mono.just(Empty.getDefaultInstance)
  }

  /**
    * <pre>
    * Single Request /Streaming Response
    * </pre>
    */
  override def requestStream(message: SimpleRequest, metadata: ByteBuf): Flux[SimpleResponse] = {
    val requestMessage = message.getRequestMessage
    Flux.interval(Duration.ofMillis(200))
      .onBackpressureDrop()
      .map(i => i + " - got message - " + requestMessage)
      .map(s => {
        logger.info(s"s = ${s}")
        SimpleResponse.newBuilder().setResponseMessage(s.getResponseMessage).build()
      })
  }

  /**
    * <pre>
    * Streaming Request / Single Response
    * </pre>
    */
  override def streamingRequestSingleResponse(messages: Publisher[SimpleRequest], metadata: ByteBuf): Mono[SimpleResponse] = {
    RsocketRpcHelper.streamingRequestSingleResponse(messages, metadata())
  }

  /**
    * <pre>
    * Streaming Request / Streaming Response
    * </pre>
    */
  override def streamingRequestAndResponse(messages: Publisher[SimpleRequest], metadata: ByteBuf): Flux[SimpleResponse] = {
    Flux.from(messages).flatMap(e => requestReply(e, metadata))
  }
}
