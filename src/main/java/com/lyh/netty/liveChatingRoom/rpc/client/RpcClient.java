package com.lyh.netty.liveChatingRoom.rpc.client;


import com.lyh.netty.liveChatingRoom.asm.protocol.MessageCodecSharable;
import com.lyh.netty.liveChatingRoom.asm.protocol.ProcotolFrameDecoder;
import com.lyh.netty.liveChatingRoom.rpc.handler.RpcResponseMessageHandler;
import com.lyh.netty.liveChatingRoom.rpc.message.RpcRequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable(); // 【使用 asm包方法】

        // rpc 响应消息处理器，待实现
        RpcResponseMessageHandler RPC_RESPONSE_HANDLER = new RpcResponseMessageHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder()); // 【使用 asm包方法】
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast(RPC_RESPONSE_HANDLER);
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();

            final ChannelFuture future = channel.writeAndFlush(
                    new RpcRequestMessage(
                            1,
                            "com.lyh.netty.liveChatingRoom.rpc.server.service.HelloService",
                            "sayHello",
                            String.class,
                            new Class[]{String.class},
                            new Object[]{"helloworld!"}
                    )
                    // 发送不成功  【打印错误信息】
            ).addListener(promise->{
                if(!promise.isSuccess()){
                    Throwable cause = promise.cause();
                    log.error("error : ", cause);
                }else{
                    log.debug("rpc调用成功");
                }
            });

            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
