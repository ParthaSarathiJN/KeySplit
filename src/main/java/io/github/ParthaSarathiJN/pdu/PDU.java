package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.*;

public abstract class PDU {

	private PDUHeader pduHeader = null;
	private RequestPacket requestPacket = null;
	private ResponsePacket responsePacket = null;

//	public PDU(byte operation) {
//		setOperation(operation);
//	}

	public ByteBuffer getData() {
		return null;
	}

	public void setData(ByteBuffer buffer) {
		return;
	}

	public boolean canResponse() {
		return false;
	}

	public void setOperation(byte operation) {
		pduHeader.setOperation(operation);
	}

//	public PDU createPdu(ByteBuffer buffer) {
//
//		pduHeader.setData(buffer);
//
//		if (pduHeader.getOperation() > 0) {
//			requestPacket = new RequestPacket();
//			requestPacket.setData(buffer);
//			PDU pdu = createImplPdu(pduHeader.getOperation(), buffer, requestPacket.getKeyLength());
//			pdu.setData(buffer);
//		} else {
//			responsePacket = new ResponsePacket();
//			responsePacket.setData(buffer);
////			PDU pdu = createImplPdu(pduHeader.getOperation());
////			pdu.setData(buffer);
//		}
//
//		return pduHeader;
//	}

	public PDU createImplPdu(byte operation, ByteBuffer buffer, int keyLength) {
		switch (operation) {
			case GET_REQ:
				byte[] keyBytes = new byte[keyLength];
				buffer.get(keyBytes);
				return new GetRequest(keyBytes);
//			case INSERT_REQ:
//				return new InsertRequest();
//			case UPDATE_REQ:
//				return new UpdateRequest();
//			case DELETE_REQ:
//				return new DeleteRequest();
//			case GET_RESP:
//				return new GetResponse();
//			case INSERT_RESP:
//				return new InsertResponse();
//			case UPDATE_RESP:
//				return new UpdateResponse();
//			case DELETE_RESP:
//				return new DeleteResponse();
			default:
				throw new IllegalArgumentException("Unknown operation: " + operation);
		}
	}

}
