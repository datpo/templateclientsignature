package cybersignclient.cyber.endpoint;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cybersignclient.cyber.ooxml.OOXMLSigner;
import cybersignclient.hmac.ApiClient;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

public class CyberOffice {
    public CyberOffice() {
    }

    public byte[] SignHashOfficeByCyberHsm(String base64Office, String host, String apiKey, String pass) {
        try {
            ApiClient client = null;
            HttpResponse get = null;
            client = new ApiClient(host);
            client.setCredentials(apiKey, pass);
            get = client.Get("/api/account/endcert");
            String certRes = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
            X509Certificate x509Cert = convertToX509Cert(certRes);
            X509Certificate[] chain = new X509Certificate[]{x509Cert};
            byte[] byteTKhai = DatatypeConverter.parseBase64Binary(base64Office);
            OOXMLSigner ooxmlSigner = new OOXMLSigner();
            byte[] signed = ooxmlSigner.signOOXMLFile(byteTKhai, (PrivateKey)null, chain, (Provider)null);
            Map<String, String> mapreq = new HashMap();
            mapreq.put("payload", DatatypeConverter.printBase64Binary(signed));
            mapreq.put("alg", "SHA1WithRSA");
            get = client.Post("/api/bin/sign/base64", mapreq);
            String signature = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
            System.out.println(signature);
            Type type = (new TypeToken<Map<String, String>>() {
            }).getType();
            Map<String, String> myMap = (Map)(new Gson()).fromJson(signature, type);
            String signbase = (String)myMap.get("obj");
            byte[] signedFileContent = ooxmlSigner.signOOXMLFile(DatatypeConverter.parseBase64Binary(signbase), chain);
            return signedFileContent;
        } catch (Exception var20) {
            var20.printStackTrace();
            return null;
        }
    }

    private static X509Certificate convertToX509Cert(String certificateString) throws CertificateException {
        X509Certificate certificate = null;
        CertificateFactory cf = null;

        try {
            if (certificateString != null && !certificateString.trim().isEmpty()) {
                certificateString = certificateString.replace("-----BEGIN CERTIFICATE-----\n", "").replace("-----END CERTIFICATE-----", "");
                byte[] certificateData = DatatypeConverter.parseBase64Binary(certificateString);
                cf = CertificateFactory.getInstance("X509");
                certificate = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(certificateData));
            }

            return certificate;
        } catch (CertificateException var4) {
            throw new CertificateException(var4);
        }
    }
}
