package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

public interface PDUBase extends PDUPacket {

    ByteBuffer getData();

    void setData(ByteBuffer buffer);

    int calculateLength();

}
