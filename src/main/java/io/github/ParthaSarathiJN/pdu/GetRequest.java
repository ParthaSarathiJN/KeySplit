package io.github.ParthaSarathiJN.pdu;

//import io.github.ParthaSarathiJN.pdu.tlv.TLV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.*;

public class GetRequest implements PDUPacket {

	private static final Logger logger = LoggerFactory.getLogger(GetRequest.class);

	private PDU pdu;

    public GetRequest() {}

	public GetRequest(byte[] keyBytes) {
		PDUHeader pduHeader = new PDUHeader(GET_REQ);
		PDUBase pduBase = new RequestPacket(keyBytes);
		pduHeader.setLength(pduHeader.calculateLength() + pduBase.calculateLength() + this.calculateLength());
		this.pdu = new PDU(pduHeader, pduBase, this);
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

	public PDU getPdu() {
		return this.pdu;
	}
}
