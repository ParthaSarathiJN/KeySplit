package io.github.ParthaSarathiJN.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyValueStoreServer {

    private static final Logger logger = LoggerFactory.getLogger(KeyValueStoreServer.class);

    private final int applicationPort;
    private final ExecutorService threadPool;
    private final StoreKeyValue storeKeyValue;

    public KeyValueStoreServer(int applicationPort, int threadPoolSize) {
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.storeKeyValue = new StoreKeyValue();
        this.applicationPort = applicationPort;
    }

    public void startServer() {

        try {

            ServerSocket serverSocket = new ServerSocket(applicationPort);
            logger.info("Starting server socket on port: {}", applicationPort);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(new SessionWorker(clientSocket, storeKeyValue));
                } catch (IOException ioException) {
                    logger.error("IOException in Socket!");
                }
            }

        } catch (IOException ioServerSocketException) {
            logger.error("IoServerSocketException in ServerSocket!");
        }
    }

    public void stopServer() {

    }
}
