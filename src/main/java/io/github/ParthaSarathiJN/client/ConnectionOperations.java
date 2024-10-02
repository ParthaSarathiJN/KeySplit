package io.github.ParthaSarathiJN.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionOperations {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionOperations.class);


    public ConnectionOperations() {}

    public Socket connectToServer(String serverHost, int serverPort) throws IOException {
        return new Socket(serverHost, serverPort);
    }

    public void closeConnection(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        if ((socket != null) && !socket.isClosed()) {
            socket.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        logger.info("Connection to Server Closed!");
    }

}
