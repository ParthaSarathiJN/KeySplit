package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

public class ResponsePacket extends PDU {

    private int status;

//    public ResponsePacket(byte operation) {
//        super(operation);
//    }

    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(getStatus());
        return buffer;
    }

    public void setData(ByteBuffer buffer) {
        this.status = buffer.getInt();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
