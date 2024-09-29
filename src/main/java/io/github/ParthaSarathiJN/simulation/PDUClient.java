package io.github.ParthaSarathiJN.simulation;

import io.github.ParthaSarathiJN.pdu.PDUHeader;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PDUClient {

	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 8080)) {
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();

			// Create and set PDUHeader
			PDUHeader pduHeader = new PDUHeader();
			pduHeader.setLength(21);  // Example length
			pduHeader.setOperation((byte) 1);  // Example operation code
			pduHeader.setUuidByteArr(ByteBuffer.wrap(pduHeader.generateUUID()));  // Example UUID

			// Get ByteBuffer from PDUHeader and send it over socket
			ByteBuffer buffer = pduHeader.getData();
			out.write(buffer.array());  // Send the byte array over the socket
			out.flush();

			byte[] headerBytes = new byte[21];
			in.read(headerBytes);
			ByteBuffer buffer2 = ByteBuffer.wrap(headerBytes);
			PDUHeader pduHeaderResp = new PDUHeader();
			pduHeaderResp.setData(buffer2);

			// Verify the received header
			System.out.println("Received PduHeaderResp:");
			System.out.println("Resp Length: " + pduHeaderResp.getLength());
			System.out.println("Resp Operation: " + pduHeaderResp.getOperation());
			System.out.println("Resp UUID: " + pduHeaderResp.getUuid());

			System.out.println("PDUHeader received!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
