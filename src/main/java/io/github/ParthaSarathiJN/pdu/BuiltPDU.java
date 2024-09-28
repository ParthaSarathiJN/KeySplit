package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

public class BuiltPDU {

	private PDUHeader pduHeader;
	private PDUPacket basePacket;
	private PDUPacket implPacket;

	public BuiltPDU(PDUHeader pduHeader, PDUPacket basePacket, PDUPacket implPacket) {

		this.pduHeader = pduHeader;
		this.basePacket = basePacket;
		this.implPacket = implPacket;

//		int baseCapacity = (basePacket != null && basePacket.getData() != null) ? basePacket.getData().capacity() : 0;
//		int implCapacity = (implPacket != null && implPacket.getData() != null) ? implPacket.getData().capacity() : 0;
//		int totalLength = pduHeader.getData().capacity() + baseCapacity + implCapacity;

//		pduHeader.setLength(totalLength);

		pduHeader.setLength(pduHeader.calculateLength() + basePacket.calculateLength() + implPacket.calculateLength());
	}

	public ByteBuffer getData() {
		// Calculate the total length first
		int totalLength = (pduHeader != null && pduHeader.getData() != null ? pduHeader.getData().capacity() : 0) +
				(basePacket != null && basePacket.getData() != null ? basePacket.getData().capacity() : 0) +
				(implPacket != null && implPacket.getData() != null ? implPacket.getData().capacity() : 0);

		// Allocate ByteBuffer with total length
		ByteBuffer buffer = ByteBuffer.allocate(totalLength);

		// Put the data into the buffer, handling null cases
		buffer.put(pduHeader.getData());
		buffer.put(basePacket.getData());
		buffer.put(implPacket.getData());

		buffer.flip();  // Prepare buffer for reading
		return buffer;
	}

	public static BuiltPDU setData(ByteBuffer buffer, Class<? extends PDUPacket> basePacketClass, Class<? extends PDUPacket> implPacketClass) {
		try {
			// Deserialize the PDUHeader
			PDUHeader header = new PDUHeader();
			header.setData(buffer);

			// Deserialize the base packet
			PDUPacket basePacket = basePacketClass.getDeclaredConstructor().newInstance();
			basePacket.setData(buffer);

			// Deserialize the implementation packet
			PDUPacket implPacket = implPacketClass.getDeclaredConstructor().newInstance();
			implPacket.setData(buffer);

			// Create the full PDU object with the deserialized components
			return new BuiltPDU(header, basePacket, implPacket);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public PDUHeader getPDUHeader() {
		return pduHeader;
	}

	public PDUPacket getBasePacket() {
		return basePacket;
	}

	public PDUPacket getImplPacket() {
		return implPacket;
	}

//	public ByteBuffer getData() {
//		return null;
//	}

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

//	public PDU createImplPdu(byte operation, ByteBuffer buffer, int keyLength) {
//		switch (operation) {
//			case GET_REQ:
//				byte[] keyBytes = new byte[keyLength];
//				buffer.get(keyBytes);
//				return new GetRequest(keyBytes);
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
//			default:
//				throw new IllegalArgumentException("Unknown operation: " + operation);
//		}
//	}

}
