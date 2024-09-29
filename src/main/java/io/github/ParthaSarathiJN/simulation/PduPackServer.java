package io.github.ParthaSarathiJN.simulation;

import io.github.ParthaSarathiJN.pdu.*;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;

public class PduPackServer {

	private static final Logger logger = LoggerFactory.getLogger(PduPackServer.class);

	public static void main(String[] args) {

		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			logger.trace("Server listening on port 8080...");

			while (true) {
				Socket socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();

				// Receive and deserialize PDU
				byte[] pduBytes = new byte[100];  // Adjust size accordingly
				int bytesRead = in.read(pduBytes);
				logger.debug("Bytes received: " + bytesRead);
				ByteBuffer buffer = ByteBuffer.wrap(pduBytes);
				logger.info("Received raw data: " + Arrays.toString(pduBytes));


				// Deserialize based on expected packet types
				BuiltPDU builtPdu = BuiltPDU.setData(buffer, RequestPacket.class, GetRequest.class);

				// Verify the received PDU
				logger.info("Received PDU:");
				logger.info("Header Length: " + builtPdu.getPDUHeader().getLength());
				logger.info("Request Key: " + new String(((RequestPacket) builtPdu.getBasePacket()).getKeyBytes()));

				// Create a response PDU
				PDUHeader responseHeader = new PDUHeader();
				responseHeader.setOperation((byte) -1);
				responseHeader.setUuidByteArr(ByteBuffer.wrap(builtPdu.getPDUHeader().getUuidByteArr()));

				// Create a ResponsePacket and GetResponse (or other response types)
				ResponsePacket responsePacket = new ResponsePacket(0);
				GetResponse getResponse = new GetResponse("responseKeyData".getBytes(), 0);  // Assume similar to ResponsePacket

				// Create the full response PDU
				BuiltPDU responseBuiltPDU = new BuiltPDU(responseHeader, responsePacket, getResponse);

				// Send the response
				ByteBuffer responseBuffer = responseBuiltPDU.getData();
				out.write(responseBuffer.array());
				out.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
