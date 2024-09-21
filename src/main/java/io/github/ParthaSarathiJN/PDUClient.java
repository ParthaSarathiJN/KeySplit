package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.pdu.PDUHeader;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PDUClient {

	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 8080)) {
			OutputStream out = socket.getOutputStream();

			// Create and set PDUHeader
			PDUHeader pduHeader = new PDUHeader();
			pduHeader.setLength(21);  // Example length
			pduHeader.setOperation((byte) 1);  // Example operation code
			pduHeader.setUuidByteArr(ByteBuffer.wrap(pduHeader.generateUUID()));  // Example UUID

			// Get ByteBuffer from PDUHeader and send it over socket
			ByteBuffer buffer = pduHeader.getData();
			out.write(buffer.array());  // Send the byte array over the socket

			System.out.println("PDUHeader sent!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
