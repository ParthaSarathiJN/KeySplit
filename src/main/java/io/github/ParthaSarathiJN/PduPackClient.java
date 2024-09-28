package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.pdu.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PduPackClient {

	private static final Logger logger = LoggerFactory.getLogger(PduPackClient.class);

	public static void main(String[] args) {

		try (Socket socket = new Socket("localhost", 8080)) {
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();

			// Create PDUHeader
			PDUHeader pduHeader = new PDUHeader();
			pduHeader.setLength(21);  // Example length
			pduHeader.setOperation((byte) 1);  // Example operation code
			pduHeader.setUuidByteArr(ByteBuffer.wrap(pduHeader.generateUUID()));  // Example UUID

			// Create RequestPacket and GetRequest (or other packet types)
			RequestPacket requestPacket = new RequestPacket("test request".getBytes());
//			pduHeader.setLength(21 + requestPacket.calculateLength());
			GetRequest getRequest = new GetRequest();  // Assume similar to RequestPacket

			// Create the full PDU
			BuiltPDU builtPdu = new BuiltPDU(pduHeader, requestPacket, getRequest);

			// Send the PDU
			ByteBuffer buffer = builtPdu.getData();  // PDU serialization
			byte[] pduBytes = buffer.array();
			logger.info("Client sending data: " + Arrays.toString(pduBytes));
			out.write(pduBytes);
			out.flush();

//			byte[] pduBytes = buffer.array();
//			logger.info("Sending raw data: " + Arrays.toString(pduBytes));


			// Receive and deserialize the response PDU
			byte[] responseBytes = new byte[1024];  // Adjust size accordingly
			in.read(responseBytes);
			ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);

			// Deserialize the PDU based on expected packet types
			BuiltPDU responseBuiltPDU = BuiltPDU.setData(responseBuffer, ResponsePacket.class, GetResponse.class);

			// Verify the response
			logger.error("Received PDU:");
			logger.info("Header Length: " + responseBuiltPDU.getPDUHeader().getLength());
			logger.info("Response Key: " + new String(((GetResponse) responseBuiltPDU.getImplPacket()).getValueBytes()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
