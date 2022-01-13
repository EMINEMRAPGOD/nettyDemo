package com.lyh.netty.liveChatingRoom.asm.message;

public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
