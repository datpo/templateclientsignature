package cybersignclient.hmac;

import java.util.List;

public class PdfListHashData {
    private List<String> base64hash;
    private String hashalg;
    private OtpChallenge otpChallenge;

    public List<String> getBase64hash() {
        return base64hash;
    }
    public void setBase64hash(List<String> base64hash) {
        this.base64hash = base64hash;
    }

    public String getHashalg() {
        return hashalg;
    }
    public void setHashalg(String hashalg) {
        this.hashalg = hashalg;
    }

    public OtpChallenge getOtpChallenge() {
        return otpChallenge;
    }

    public void setOtpChallenge(OtpChallenge otpChallenge) {
        this.otpChallenge = otpChallenge;
    }
}
