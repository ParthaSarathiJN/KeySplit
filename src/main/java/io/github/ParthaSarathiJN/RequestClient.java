package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.pdu.PDUHeader;
import io.github.ParthaSarathiJN.pdu.RequestPacket;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class RequestClient {

	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 8080)) {
			OutputStream out = socket.getOutputStream();

			// Create and set PDUHeader
			PDUHeader pduHeader = new PDUHeader();
			pduHeader.setLength(21);  // Example length
			pduHeader.setOperation((byte) 1);  // Example operation code
			pduHeader.setUuidByteArr(ByteBuffer.wrap(new byte[16]));  // Example UUID

			// Create and set RequestPacket
			byte[] keyBytes = "exampleKeyPartha".getBytes();  // Example key
			RequestPacket requestPacket = new RequestPacket(keyBytes);

			pduHeader.setLength(requestPacket.calculateLength() + 21);

			// Get ByteBuffer from PDUHeader and RequestPacket and send them over the socket
			ByteBuffer headerBuffer = pduHeader.getData();
			ByteBuffer requestBuffer = requestPacket.getData();

			// Print buffer details for debugging
			System.out.println("Header Buffer size: " + headerBuffer.capacity());
			System.out.println("Request Buffer size: " + requestBuffer.capacity());

			// Send both buffers
			out.write(headerBuffer.array());  // Send PDUHeader
			out.write(requestBuffer.array());  // Send RequestPacket

			System.out.println("PDUHeader and RequestPacket sent!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
