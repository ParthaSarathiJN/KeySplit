package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.GET_REQ;

public class GetRequest implements PDUPacket {

    public GetRequest() {
    }

	@Override
	public ByteBuffer getData() {
		return ByteBuffer.allocate(0);
	}

	@Override
	public void setData(ByteBuffer buffer) {
		return;
	}

	public int calculateLength() {
		return 0;
	}
}
