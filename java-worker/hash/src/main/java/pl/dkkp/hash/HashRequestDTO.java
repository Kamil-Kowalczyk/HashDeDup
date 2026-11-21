package pl.dkkp.hash;

public record HashRequestDTO(
        String jobId,
        String taskId,
        HashAlgorithm hashAlgorithm,
        String fileName
) {}
