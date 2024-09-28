package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static io.github.ParthaSarathiJN.common.Constants.INSERT_REQ;

public class InsertRequest implements PDUPacket {

    Logger logger = LogManager.getLogManager().getLogger("InsertRequest");

    private int valueLength;
    private byte[] valueBytes;

    public InsertRequest(byte[] valueBytes) {
        this.valueLength = valueBytes.length;
        this.valueBytes = valueBytes;
    }

    @Override
    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + valueLength);
        buffer.putInt(valueLength);
        buffer.put(valueBytes);
        System.out.println("Serialized valueLength InsertRequest: " + valueLength);
        System.out.println("Serialized valueBytes in InsertRequest: " + new String(valueBytes));
        buffer.flip();
        return buffer;
    }

    @Override
    public void setData(ByteBuffer buffer) {
        this.valueLength = buffer.getInt();
        logger.info("Deserialized valueLength in InsertRequest: " + this.valueLength);
        if (valueLength > 0) {
            this.valueBytes = new byte[valueLength];
            buffer.get(this.valueBytes);      // Deserialize key bytes
            System.out.println("Deserialized valueBytes in RequestPacket: " + new String(this.valueBytes));
        } else {
            valueBytes = new byte[0];  // Handle empty case
        }
    }

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
        byte[] valueBytes = new byte[valueLength];
        buffer.get(valueBytes);
        this.valueBytes = valueBytes;
    }

    @Override
    public int calculateLength() {
        return 4 + valueLength;
    }
}
