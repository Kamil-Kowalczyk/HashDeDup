package pl.dkkp.hash;

import java.util.Objects;

public class Properties {
    private String redisHost;
    private int redisPort;
    private String redisTaskChannel;

    public Properties() {
        this.redisHost = Objects.requireNonNullElse(System.getenv("REDIS_HOST"), "localhost");
        String redisPortRaw = Objects.requireNonNullElse(System.getenv("REDIS_PORT"), "6379");
        try {
            this.redisPort = Integer.parseInt(redisPortRaw);
        } catch (NumberFormatException e) {
            this.redisPort = 6379;
        }
        this.redisTaskChannel = Objects.requireNonNullElse(System.getenv("REDIS_JAVA_CHANNEL"), "java-worker");
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
}
