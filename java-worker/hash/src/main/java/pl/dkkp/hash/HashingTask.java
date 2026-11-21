package pl.dkkp.hash;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class HashingTask implements Runnable {
    private final Logger log = LoggerFactory.getLogger(HashingTask.class);
    private final ObjectMapper objectMapper;
    private final String rawRequest;
    private final RedisAsyncCommands<String, String> redisAsyncCommands;
    private final String resultChannel;
    private final String filesDir;

    public HashingTask(String message, ObjectMapper objectMapper, RedisAsyncCommands<String, String> redisAsyncCommands, String resultChannel, String filesDir) {
        this.rawRequest = message;
        this.objectMapper = objectMapper;
        this.redisAsyncCommands = redisAsyncCommands;
        this.resultChannel = resultChannel;
        this.filesDir = filesDir;
    }

    @Override
    public void run() {
        try {
            HashRequestDTO request = objectMapper.readValue(rawRequest, HashRequestDTO.class);
            log.info("Received hash request {}", request);
            long start = Instant.now().toEpochMilli();
            String result = FileHasher.hash(null, request.hashAlgorithm());
            long end = Instant.now().toEpochMilli();
            long timeProcessing = end - start;
            log.info("Hash request with id {} end, result {} in time {}ms", request.taskId(), result, timeProcessing);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
