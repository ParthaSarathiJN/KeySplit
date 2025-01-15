package io.github.ParthaSarathiJN.pdu.tlv;

import java.nio.ByteBuffer;

public class TLVInt extends TLV{

    private int value = 0;

    public TLVInt(byte p_tag) {
        super(p_tag, 4, 4);
    }

    public TLVInt(byte p_tag, int p_value) {
        super(p_tag, 4, 4);
        value = p_value;
        markValueSet();
    }

    public void setValueData(ByteBuffer buffer) {
        value = buffer.getInt();
        markValueSet();
    }

    public ByteBuffer getValueData() {
        ByteBuffer valueBuf = ByteBuffer.allocate(4);
        return valueBuf.putInt(getValue());
    }

    public void setValue(int p_value) {
        value = p_value;
        markValueSet();
    }

    public int getValue() {
        if (hasValue()) {
            return value;
        }
        else
            return 0;
    }
}
