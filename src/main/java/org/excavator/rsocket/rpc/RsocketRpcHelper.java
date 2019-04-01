package org.excavator.rsocket.rpc;

import io.netty.buffer.ByteBuf;
import org.excavator.grpc.rsocket.rpc.SimpleRequest;
import org.excavator.grpc.rsocket.rpc.SimpleResponse;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class RsocketRpcHelper {
    public static Mono<SimpleResponse> streamingRequestSingleResponse(Publisher<SimpleRequest> messages, ByteBuf metdata){
        return Flux.from(messages)
                .windowTimeout(10, Duration.ofSeconds(500))
                .take(1)
                .flatMap(Function.identity())
                .reduce(
                        new ConcurrentHashMap<Character, AtomicInteger>(),
                        (map, s) -> {
                            char[] chars = s.getRequestMessage().toCharArray();

                            for(char c : chars){
                                map.computeIfAbsent(c, _c -> new AtomicInteger()).incrementAndGet();
                            }

                            return map;
                        }
                ).map(map -> {
                    StringBuilder builder = new StringBuilder();
                    map.forEach(((character, atomicInteger) -> {
                        builder.append("charactor -> ").append(character)
                                .append(", count -> ").append(atomicInteger.get())
                                .append("\n");
                    }));
                    String s= builder.toString();

                    return SimpleResponse.newBuilder().setResponseMessage(s).build();
        });
    }

    public static Flux<SimpleResponse> requestStream(SimpleRequest message, ByteBuf metadata) {
        String requestMessage = message.getRequestMessage();

        return Flux.interval(Duration.ofMillis(200))
                .onBackpressureDrop()
                .map(i -> i + " - got message -" + requestMessage)
                .map(s -> SimpleResponse.newBuilder().setResponseMessage(s).build());
    }

    public static Flux<SimpleRequest> buildRequests(){
        return Flux.range(1, 11)
                .map(i -> "sending -> " + i)
                .map(s -> SimpleRequest.newBuilder().setRequestMessage(s).build());
    }

    public static SimpleRequest buildRequest(){
        return SimpleRequest.newBuilder().setRequestMessage("simpleRequest").build();
    }

}
