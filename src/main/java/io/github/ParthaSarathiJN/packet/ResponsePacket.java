package io.github.ParthaSarathiJN.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class ResponsePacket extends BasePacket {

    private byte status;
    private String errorMessage;

    public ResponsePacket(byte operation, byte status, String errorMessage) {
        super(operation);
        this.status = status;
        this.errorMessage = errorMessage;
    }

    @Override
    public void fromByteArray(byte[] data) {
        super.fromByteArray(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        this.status = buffer.get();
        this.errorMessage = Arrays.toString(buffer.array());
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public int calculateLength() {
        return super.calculateLength() + 1 + (errorMessage.length() * 2);
    }
}
