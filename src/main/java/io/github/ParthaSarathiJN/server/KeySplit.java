package io.github.ParthaSarathiJN.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class KeySplit {

    private static final Logger logger = LoggerFactory.getLogger(KeySplit.class);

    public static ConcurrentHashMap<ByteBuffer, byte[]> keySplit = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {

        KeySplit keySplit = new KeySplit();

        keySplit.startKeySplit();
    }

    private void startKeySplit() throws IOException {

        int[] properties = fetchAppProperties();
        int applicationPort = properties[0];
        int threadPoolSize = properties[1];

        logger.info("Fetched Properties from Properties file!");

        KeyValueStoreServer keyValServer = new KeyValueStoreServer(applicationPort, threadPoolSize);
        keyValServer.startServer();
    }

    private static int[] fetchAppProperties() {
        int [] propArr = new int[2];

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("KeySplit.properties"));
            propArr[0] = Integer.parseInt(properties.getProperty("application.port", "8080"));
            propArr[1] = Integer.parseInt(properties.getProperty("thread.pool.size", "10"));
        } catch (IOException ioException) {
            logger.error("Properties File Not Found! Closing Application!");
            System.exit(1);
        }
        return propArr;
    }
}
