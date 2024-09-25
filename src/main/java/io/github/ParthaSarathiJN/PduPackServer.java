package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.pdu.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class PduPackServer {

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			System.out.println("Server listening on port 8080...");

			while (true) {
				Socket socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();

				// Receive and deserialize PDU
				byte[] pduBytes = new byte[100];  // Adjust size accordingly
				int bytesRead = in.read(pduBytes);
				System.out.println("Bytes received: " + bytesRead);
				ByteBuffer buffer = ByteBuffer.wrap(pduBytes);
				System.out.println("Received raw data: " + Arrays.toString(pduBytes));


				// Deserialize based on expected packet types
				PDU pdu = PDU.setData(buffer, RequestPacket.class, GetRequest.class);

				// Verify the received PDU
				System.out.println("Received PDU:");
				System.out.println("Header Length: " + pdu.getPDUHeader().getLength());
				System.out.println("Request Key: " + new String(((RequestPacket) pdu.getBasePacket()).getKeyBytes()));

				// Create a response PDU
				PDUHeader responseHeader = new PDUHeader();
				responseHeader.setOperation((byte) -1);
				responseHeader.setUuidByteArr(ByteBuffer.wrap(pdu.getPDUHeader().getUuidByteArr()));

				// Create a ResponsePacket and GetResponse (or other response types)
				ResponsePacket responsePacket = new ResponsePacket(0);
				GetResponse getResponse = new GetResponse("responseKeyData".getBytes());  // Assume similar to ResponsePacket

				// Create the full response PDU
				PDU responsePDU = new PDU(responseHeader, responsePacket, getResponse);

				// Send the response
				ByteBuffer responseBuffer = responsePDU.getData();
				out.write(responseBuffer.array());
				out.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
