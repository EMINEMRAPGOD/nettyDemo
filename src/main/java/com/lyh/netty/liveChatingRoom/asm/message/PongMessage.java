package com.lyh.netty.liveChatingRoom.asm.message;

public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
