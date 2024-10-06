package io.github.ParthaSarathiJN.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class KeySplitClient {

    private static final Logger logger = LoggerFactory.getLogger(KeySplitClient.class);

    private final String SERVER_HOST = "localhost";
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ConnectionOperations connectionOperations;
    private CreateRequest createRequest;

    public static void main(String[] args) throws IOException {

        KeySplitClient keySplitClient = new KeySplitClient();

        keySplitClient.startKeySplitClient();
    }

    private void startKeySplitClient() throws IOException {

        int[] properties = fetchAppProperties();
        int serverPort = properties[0];

        logger.info("Fetched Properties from Properties file!");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        connectionOperations = new ConnectionOperations();

        while (true) {

            setUpConToServer(serverPort);

            while (running) {

                createRequest = new CreateRequest(inputStream, outputStream);

                System.out.println("\nChoose an operation:\n1. GET\n2. INSERT\n3. UPDATE\n4. DELETE");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        createRequest.sendGetRequest(scanner);
                        break;
                    case "2":
                        createRequest.sendInsertRequest(scanner);
                        break;
                    case "3":
                        createRequest.sendUpdateRequest(scanner);
                        break;
                    case "4":
                        createRequest.sendDeleteRequest(scanner);
                        break;
                    default:
                        logger.info("Exiting Client Instance...");
                        running = false;
                        break;
                }
            }
            connectionOperations.closeConnection(socket, inputStream, outputStream);
        }
    }

    private void setUpConToServer(int serverPort) throws IOException {
        socket = connectionOperations.connectToServer(SERVER_HOST, serverPort);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    private static int[] fetchAppProperties() {
        int [] propArr = new int[2];

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("KeySplit.properties"));
            propArr[0] = Integer.parseInt(properties.getProperty("application.port", "8080"));
        } catch (IOException ioException) {
            logger.error("Properties File Not Found! Closing Application!");
            System.exit(1);
        }
        return propArr;
    }

}
