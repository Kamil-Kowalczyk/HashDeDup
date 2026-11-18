package pl.dkkp.hash;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class WorkerApplication {
    private static final Logger log = LoggerFactory.getLogger(WorkerApplication.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        RedisManager redisManager = new RedisManager(properties.getRedisHost(), properties.getRedisPort());
        CountDownLatch shutdownLatch = new CountDownLatch(1);
        try (StatefulRedisPubSubConnection<String, String> connection = redisManager.connectPubSub()) {
            connection.addListener(new RedisPubSubAdapter<>() {
                @Override
                public void message(String channel, String message) {
                    log.debug("Received message from channel [{}]: {}", channel, message);
                }
            });
            connection.sync().subscribe(properties.getRedisTaskChannel());
            shutdownLatch.await();
        } catch (Exception e) {
            log.error("Error when connecting to redis client", e);
        } finally {
            redisManager.shutdown();
        }
    }
}
