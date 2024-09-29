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

    private int applicationPort;
    private ExecutorService threadPool;
    private StoreKeyValue storeKeyValue;

    public KeyValueStoreServer(int applicationPort, int threadPoolSize) {
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.storeKeyValue = new StoreKeyValue();
        this.applicationPort = applicationPort;
    }

    public void startServer() throws IOException {

        ServerSocket serverSocket = new ServerSocket(applicationPort);

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new SessionWorker(clientSocket, storeKeyValue));
            } catch (IOException ioException) {
                logger.error("IOException in Socket!");
            }
        }
    }

    public void stopServer() {

    }
}
