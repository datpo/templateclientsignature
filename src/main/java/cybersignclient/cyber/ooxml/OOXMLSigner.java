package cybersignclient.cyber.ooxml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OOXMLSigner extends AbstractOOXMLSignatureService {
    private OOXMLTestDataStorage temporaryDataStorage = new OOXMLTestDataStorage();
    private ByteArrayOutputStream signedOOXMLOutputStream = new ByteArrayOutputStream();
    private byte[] ooxmlData;

    static {
        OOXMLProvider.install();
    }

    public OOXMLSigner() {
    }

    public byte[] signOOXMLFile(byte[] fileData, PrivateKey privateKey, Certificate[] certificateChain, Provider provSign) throws Exception {
        this.ooxmlData = fileData;
        new LinkedList();
        DigestInfo digestInfo = this.preSign((List)null, (List)null);
        byte[] getData = digestInfo.digestValue;
        return getData;
    }

    public byte[] signOOXMLFile(byte[] signatureValue, Certificate[] certificateChain) throws Exception {
        X509Certificate[] signingcertChain = new X509Certificate[certificateChain.length];

        for(int i = 0; i < certificateChain.length; ++i) {
            signingcertChain[i] = (X509Certificate)certificateChain[i];
        }

        this.postSign(signatureValue, Arrays.asList(signingcertChain));
        byte[] signedOOXMLData = this.getSignedOfficeOpenXMLDocumentData();
        return signedOOXMLData;
    }

    protected OutputStream getSignedOfficeOpenXMLDocumentOutputStream() {
        return this.signedOOXMLOutputStream;
    }

    protected InputStream getOfficeOpenXMLDocumentInputStream() {
        byte[] buff = new byte[this.ooxmlData.length];
        System.arraycopy(this.ooxmlData, 0, buff, 0, this.ooxmlData.length);
        return new ByteArrayInputStream(buff);
    }

    protected TemporaryDataStorage getTemporaryDataStorage() {
        return this.temporaryDataStorage;
    }

    public byte[] getSignedOfficeOpenXMLDocumentData() {
        return this.signedOOXMLOutputStream.toByteArray();
    }

    public void setOoxmlData(byte[] ooxmlData) {
        this.ooxmlData = ooxmlData;
    }

    private static class Algorithm {
        String algo;
        AlgorithmParameterSpec param;

        public Algorithm(String algo, AlgorithmParameterSpec param) {
            this.algo = algo;
            this.param = param;
        }
    }
}
