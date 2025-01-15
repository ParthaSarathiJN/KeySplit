package io.github.ParthaSarathiJN.pdu.tlv;

import java.nio.ByteBuffer;

public abstract class TLV {

    // max key size 8MB and value size 64MB

    private byte tag = 0;
    private boolean valueIsSet = false;
    /**
     * The minimal length of the data. If no min length is required, then set to -1.
     */
    private int minLength = -1;
    /**
     * The maximal length of the data. If no max length is required, then set to -1
     */
    private int maxLength = -1;

    public TLV(byte tag) {
        this.tag = tag;
    }

    public TLV(byte tag, int min, int max) {
        super();
        this.tag = tag;
        minLength = min;
        maxLength = max;
    }

    public abstract void setValueData(ByteBuffer dataBuffer);
//        value = new byte[dataBuffer.remaining()];
//        dataBuffer.get(value);
//        markValueSet();

    public abstract ByteBuffer getValueData();
//        if (value == null) return ByteBuffer.allocate(0);
//        return ByteBuffer.wrap(value);


    public void setTag(byte tag) {
        this.tag = tag;
    }

    public byte getTag() {
        return tag;
    }

    public int getLength() {
        if (hasValue()) {
            ByteBuffer valueBuf = getValueData();
            return valueBuf != null ? valueBuf.remaining() : 0;
        }
        return 0;
    }

    public void setData(ByteBuffer byteBuffer) {
        byte newTag = byteBuffer.get();
        int length = byteBuffer.getInt();
        ByteBuffer valueBuffer = byteBuffer.slice();
        valueBuffer.limit(length);
        byteBuffer.position(byteBuffer.position() + length);
        setValueData(valueBuffer);
        setTag(newTag);
    }

    // TODO need to dynamically fetch total length and divide to number of packets
    public ByteBuffer getData() {
        if (hasValue()) {
            ByteBuffer tlvBuffer = ByteBuffer.allocate(1 + 4 + getLength());
            tlvBuffer.put(getTag());
            tlvBuffer.putInt(getLength());
            tlvBuffer.put(getValueData());
            tlvBuffer.flip();
            return tlvBuffer;
        } else {
            return null;
        }
    }

    protected void markValueSet() {
        valueIsSet = true;
    }

    public boolean hasValue() {
        return valueIsSet;
    }

}
