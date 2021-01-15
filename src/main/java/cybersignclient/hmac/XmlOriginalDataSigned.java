package cybersignclient.hmac;

public class XmlOriginalDataSigned {
    private String base64xmlsigned;
    private int status;
    private String description;

    public String getBase64xmlsigned() {
        return base64xmlsigned;
    }
    public void setBase64xmlsigned(String base64xmlsigned) {
        this.base64xmlsigned = base64xmlsigned;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
