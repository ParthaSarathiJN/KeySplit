package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

public class ResponsePacket implements PDUBase {

    private int status;

    public ResponsePacket() {
    }

    public ResponsePacket(int status) {
        this.status = status;
    }

    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(getStatus());
        buffer.flip();
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

    public int calculateLength() {
        return 4;
    }
}
