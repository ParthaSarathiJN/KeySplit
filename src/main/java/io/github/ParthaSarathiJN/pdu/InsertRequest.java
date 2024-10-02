package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.ParthaSarathiJN.common.Constants.INSERT_REQ;

public class InsertRequest implements PDUPacket {

    private static final Logger logger = LoggerFactory.getLogger(InsertRequest.class);

    private PDU pdu;

    private int valueLength;
    private byte[] valueBytes;

    public InsertRequest() {}

    public InsertRequest(byte[] keyBytes, byte[] valueBytes) {
        this.valueLength = valueBytes.length;
        this.valueBytes = valueBytes;

        PDUHeader pduHeader = new PDUHeader(INSERT_REQ);
        PDUBase pduBase = new RequestPacket(keyBytes);
        pduHeader.setLength(pduHeader.calculateLength() + pduBase.calculateLength() + this.calculateLength());
        this.pdu = new PDU(pduHeader, pduBase, this);
    }

    @Override
    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + valueLength);
        buffer.putInt(valueLength);
        buffer.put(valueBytes);
        logger.info("Serialized valueLength InsertRequest: {}", valueLength);
        logger.info("Serialized valueBytes in InsertRequest: {}", new String(valueBytes));
        buffer.flip();
        return buffer;
    }

    @Override
    public void setData(ByteBuffer buffer) {
        this.valueLength = buffer.getInt();
        logger.info("Deserialized valueLength in InsertRequest: {}", this.valueLength);
        if (valueLength > 0) {
            this.valueBytes = new byte[valueLength];
            buffer.get(this.valueBytes);      // Deserialize key bytes
            logger.info("Deserialized valueBytes in RequestPacket: {}", new String(this.valueBytes));
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

    public PDU getPdu() {
        return this.pdu;
    }
}
