package pl.dkkp.hash;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisManager {
    private final RedisClient redisClient;
    private static final Logger log = LoggerFactory.getLogger(RedisManager.class);

    public RedisManager(String host, int port) {
        RedisURI uri = RedisURI.builder().redis(host, port).build();
        redisClient = RedisClient.create(uri);
        log.info("Connection to Redis set up succesfully");
    }

    public StatefulRedisConnection<String, String> connect() {
        return redisClient.connect();
    }

    public StatefulRedisPubSubConnection<String, String> connectPubSub() {
        return redisClient.connectPubSub();
    }

    public void shutdown() {
        if (redisClient != null) {
            log.info("Shutting down redis client");
            redisClient.shutdown();
        }
    }
}
