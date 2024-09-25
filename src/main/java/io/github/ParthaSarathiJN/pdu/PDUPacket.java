package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

public interface PDUPacket {

	ByteBuffer getData();
	void setData(ByteBuffer buffer);

	int calculateLength();
}
