package pl.dkkp.hash;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HashAlgorithm {
    MD5("md5"),
    SHA1("sha1"),
    SHA2("sha2");

    private final String algorithmName;

    HashAlgorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    @JsonValue
    public String getAlgorithmName() {
        return algorithmName;
    }

    @JsonCreator
    public static HashAlgorithm fromString(String value) {
        for (HashAlgorithm alg : values()) {
            if (alg.algorithmName.equals(value)) {
                return alg;
            }
        }
        throw new IllegalArgumentException("Invalid algorithm: " + value);
    }
}
