package cybersignclient.hmac;

public class PdfHashData {
    private String base64hash;
    private String hashalg;

    public String getBase64hash() {
        return base64hash;
    }
    public void setBase64hash(String base64hash) {
        this.base64hash = base64hash;
    }

    public String getHashalg() {
        return hashalg;
    }
    public void setHashalg(String hashalg) {
        this.hashalg = hashalg;
    }
}
