package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.pdu.PDUHeader;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PDUServer {

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			System.out.println("Server listening on port 8080...");

			while (true) {
				Socket socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();

				// Read the incoming data into a ByteBuffer
				byte[] headerBytes = new byte[21];  // Allocate enough space for the PDUHeader (21 bytes)
				in.read(headerBytes);

				// Deserialize the PDUHeader
				ByteBuffer buffer = ByteBuffer.wrap(headerBytes);
				PDUHeader pduHeader = new PDUHeader();
				pduHeader.setData(buffer);

				// Verify the received header
				System.out.println("Received PDUHeader:");
				System.out.println("Length: " + pduHeader.getLength());
				System.out.println("Operation: " + pduHeader.getOperation());
				System.out.println("UUID: " + pduHeader.getUuid());

				PDUHeader pduHeaderResp = new PDUHeader();
				pduHeaderResp.setLength(21);
				pduHeaderResp.setOperation((byte) -1);
				pduHeaderResp.setUuidByteArr(ByteBuffer.wrap(pduHeader.getUuidByteArr()));

				System.out.println("Received PDUHeader:");
				System.out.println("Resp Length: " + pduHeaderResp.getLength());
				System.out.println("Resp Operation: " + pduHeaderResp.getOperation());
				System.out.println("Resp UUID: " + pduHeaderResp.getUuid());

				ByteBuffer outBuffer = pduHeaderResp.getData();
				out.write(outBuffer.array());
				out.flush();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
