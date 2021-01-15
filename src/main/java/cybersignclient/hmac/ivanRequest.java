package cybersignclient.hmac;

public class ivanRequest {
	private String base64xml;
    private String hashalg;
    private String xpathdata;
    private String xpathsign;

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

    public String getXpathdata() {
        return xpathdata;
    }
    public void setXpathdata(String xpathdata) {
        this.xpathdata = xpathdata;
    }

    public String getXpathsign() {
        return xpathsign;
    }
    public void setXpathsign(String xpathsign) {
        this.xpathsign = xpathsign;
    }
}
