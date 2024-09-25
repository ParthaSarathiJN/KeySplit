package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;
import java.util.Objects;

import static io.github.ParthaSarathiJN.common.Constants.GET_RESP;

public class GetResponse implements PDUPacket {

    private int valueLength;
    private byte[] valueBytes;

    public GetResponse() {
    }

    public GetResponse(byte[] valueBytes) {
        this.valueLength = Objects.requireNonNull(valueBytes).length;
        this.valueBytes = valueBytes;
    }

    @Override
    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + valueLength);
        buffer.putInt(valueLength);
        buffer.put(valueBytes);
        buffer.flip();
        return buffer;
    }

    @Override
    public void setData(ByteBuffer buffer) {
        this.valueLength = buffer.getInt();
        if (valueLength > 0) {
            valueBytes = new byte[valueLength];
            buffer.get(valueBytes);
        } else {
            valueBytes = new byte[0]; // Handle empty case
        }
    }

//    public void fromByteArray(byte[] data) {
//        super.fromByteArray(data);
//        setValueLength(buffer.getInt());
//        setValueBytes(buffer);
//    }

    public int getValueLength() {
        return valueLength;
    }

    public void setValueLength(int valueLength) {
        this.valueLength = valueLength;
    }

    public byte[] getValueBytes() {
        return valueBytes;
    }

    public void setValueBytes(ByteBuffer buffer) {
        valueBytes = new byte[valueLength];
        buffer.get(valueBytes);
    }

    public int calculateLength() {
        return 4 + valueLength;
    }
}
