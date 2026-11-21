package pl.dkkp.hash;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkerApplication {
    private static final Logger log = LoggerFactory.getLogger(WorkerApplication.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        RedisManager redisManager = new RedisManager(properties.getRedisHost(), properties.getRedisPort());
        CountDownLatch shutdownLatch = new CountDownLatch(1);
        ObjectMapper objectMapper = new ObjectMapper();
        try (ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 8, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000))) {
            try (StatefulRedisPubSubConnection<String, String> pubSubConnection = redisManager.connectPubSub();
                 StatefulRedisConnection<String, String> pubConnection = redisManager.connect()) {
                executor.allowCoreThreadTimeOut(true);
                RedisAsyncCommands<String, String> asyncCommands = pubConnection.async();
                pubSubConnection.addListener(new RedisPubSubAdapter<>() {
                    @Override
                    public void message(String channel, String message) {
                        log.info("Received message {} on channel {}", message, channel);
                        try {
                            Runnable task = new HashingTask(
                                    message,
                                    objectMapper,
                                    asyncCommands,
                                    properties.getRedisResultChannel(),
                                    properties.getFilesDir()
                            );
                            executor.execute(task);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                });
                pubSubConnection.sync().subscribe(properties.getRedisTaskChannel());
                shutdownLatch.await();
            } catch (Exception e) {
                log.error("Error when connecting to redis client", e);
            } finally {
                redisManager.shutdown();
            }
        }
    }
}
