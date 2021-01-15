package cybersignclient.hmac;

/**
 * Represents contents for HMAC HTTP <code>Authorization</code> header.
 */
class AuthHeader {
    private final String algorithm;
    private final String apiKey;
    private final String nonce;
    private final byte[] digest;
    private final String timestamp;

    public AuthHeader(String algorithm, String apiKey, String nonce, String timestamp, byte[] digest) {
        this.algorithm = algorithm;
        this.apiKey = apiKey;
        this.nonce = nonce;
        this.digest = digest;
        this.timestamp = timestamp;
    }

    public AuthHeader(String algorithm, String apiKey, String timestamp, byte[] digest) {
        this(algorithm, apiKey, null, timestamp , digest);
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getApiKey() {
        return apiKey;
    }

    public byte[] getDigest() {
        return digest;
    }

    public String getNonce() {
        return nonce;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
