//package io.github.ParthaSarathiJN.pdu;
//
//import java.nio.ByteBuffer;
//import java.util.Objects;
//
//import static io.github.ParthaSarathiJN.common.Constants.INSERT_REQ;
//
//public class InsertRequest extends RequestPacket {
//
//    private int valueLength;
//    private byte[] valueBytes;
//
//    public InsertRequest(byte[] keyBytes, byte[] valueBytes) {
//        super(INSERT_REQ, keyBytes);
//        this.valueLength = Objects.requireNonNull(valueBytes).length;
//        this.valueBytes = valueBytes;
//        calculateLength();
//    }
//
//    @Override
//    protected void fromByteArray(byte[] data) {
//        super.fromByteArray(data);
//        setValueLength(buffer.getInt());
//        setValueBytes(buffer);
//    }
//
//    public int getValueLength() {
//        return valueLength;
//    }
//
//    public void setValueLength(int valueLength) {
//        this.valueLength = valueLength;
//    }
//
//    public byte[] getValueBytes() {
//        return valueBytes;
//    }
//
//    public void setValueBytes(ByteBuffer buffer) {
//        byte[] valueBytes = new byte[valueLength];
//        buffer.get(valueBytes);
//        this.valueBytes = valueBytes;
//    }
//
//    @Override
//    protected int calculateLength() {
//        return super.calculateLength() + 4 + valueLength;
//    }
//}
