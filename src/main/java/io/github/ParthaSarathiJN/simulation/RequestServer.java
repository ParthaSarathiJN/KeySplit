package io.github.ParthaSarathiJN.simulation;

import io.github.ParthaSarathiJN.pdu.PDUHeader;
import io.github.ParthaSarathiJN.pdu.RequestPacket;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class RequestServer {

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			while (true) {
				System.out.println("Server listening on port 8080...");
				try (Socket clientSocket = serverSocket.accept()) {
					InputStream in = clientSocket.getInputStream();

					// Read PDUHeader
					byte[] headerBytes = new byte[21]; // Size of the PDUHeader
					in.read(headerBytes);
					PDUHeader pduHeader = new PDUHeader();
					pduHeader.setData(ByteBuffer.wrap(headerBytes));

					// Read RequestPacket
					int requestPacketSize = pduHeader.getLength() - 21; // Adjust based on PDUHeader size
					byte[] requestBytes = new byte[requestPacketSize];
					in.read(requestBytes);

					// Create RequestPacket with the read data
					RequestPacket requestPacket = new RequestPacket(new byte[requestBytes.length]);
					requestPacket.setData(ByteBuffer.wrap(requestBytes));

					// Output received data
					System.out.println("Received PDUHeader:");
					System.out.println("Length: " + pduHeader.getLength());
					System.out.println("Operation: " + pduHeader.getOperation());
					System.out.println("Received RequestPacket with key: " + new String(requestPacket.getKeyBytes()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
