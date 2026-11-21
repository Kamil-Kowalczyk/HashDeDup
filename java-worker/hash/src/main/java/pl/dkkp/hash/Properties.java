package pl.dkkp.hash;

import java.util.Objects;

public class Properties {
    private String redisHost;
    private int redisPort;
    private String redisTaskChannel;
    private String redisResultChannel;
    private String filesDir;

    public Properties() {
        this.redisHost = Objects.requireNonNullElse(System.getenv("REDIS_HOST"), "localhost");
        String redisPortRaw = Objects.requireNonNullElse(System.getenv("REDIS_PORT"), "6379");
        try {
            this.redisPort = Integer.parseInt(redisPortRaw);
        } catch (NumberFormatException e) {
            this.redisPort = 6379;
        }
        this.redisTaskChannel = Objects.requireNonNullElse(System.getenv("REDIS_JAVA_CHANNEL"), "java-worker");
        this.filesDir = Objects.requireNonNullElse(System.getenv("FILES_DIR"), "/app/files");
        this.redisResultChannel = Objects.requireNonNullElse(System.getenv("REDIS_JAVA_RESULT_CHANNEL"), "java-worker-result");
    }

    public String getRedisHost() {
        return redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisTaskChannel() {
        return redisTaskChannel;
    }

    public String getFilesDir() {
        return filesDir;
    }

    public String getRedisResultChannel() {
        return redisResultChannel;
    }
}
