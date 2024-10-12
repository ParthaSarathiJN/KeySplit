package io.github.ParthaSarathiJN.utility;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ChannelResponse {

    private final SocketChannel socketChannel;
    private final ByteBuffer responseBuffer;

    public ChannelResponse(SocketChannel socketChannel, ByteBuffer responseBuffer) {
        this.socketChannel = socketChannel;
        this.responseBuffer = responseBuffer;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public ByteBuffer getResponseBuffer() {
        return responseBuffer;
    }
}
