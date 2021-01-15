package cybersignclient.hmac;

public class XmlInvoiceData {
    private String base64xml;
    private String hashalg;

    public String getBase64xml() {
        return base64xml;
    }
    public void setBase64xml(String base64xml) {
        this.base64xml = base64xml;
    }

    public String getHashalg() {
        return hashalg;
    }
    public void setHashalg(String hashalg) {
        this.hashalg = hashalg;
    }
}
