package pl.dkkp.hash;

import java.io.InputStream;

public class FileHasher {
    public static String hash(InputStream inputStream, HashAlgorithm hashAlgorithm) {
        return switch (hashAlgorithm) {
            case MD5 -> hashMd5(inputStream);
            case SHA1 -> hashSha1(inputStream);
            case SHA2 -> hashSha2(inputStream);
        };
    }

    private static String hashMd5(InputStream inputStream) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "HASH MD5";
    }

    private static String hashSha1(InputStream inputStream) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "HASH SHA1";
    }

    private static String hashSha2(InputStream inputStream) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "HASH SHA2";
    }
}
