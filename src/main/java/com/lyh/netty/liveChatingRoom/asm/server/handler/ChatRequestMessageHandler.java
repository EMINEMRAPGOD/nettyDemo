package com.lyh.netty.liveChatingRoom.asm.server.handler;

import com.lyh.netty.liveChatingRoom.asm.message.ChatRequestMessage;
import com.lyh.netty.liveChatingRoom.asm.message.ChatResponseMessage;
import com.lyh.netty.liveChatingRoom.asm.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 单聊---管理器
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {

        final String to = msg.getTo();
        final Channel channel = SessionFactory.getSession().getChannel(to);
        // 1. 在线
        if (channel != null){

            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
        // 2. 不在线  给发送者发送 用户不在线
        else {

            ctx.writeAndFlush(new ChatResponseMessage(false, "用户不在线！"));
        }


    }

}
