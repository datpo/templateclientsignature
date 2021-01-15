package cybersignclient.hmac;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class maintest2 {

	public static void main(String[] args) throws CertificateException {
		// TODO Auto-generated method stub
		ApiClient client = null;
		try {
			client = new ApiClient("http://10.30.1.101:8088");
			client.setCredentials("b47e7ad00b1745c3be7ad00b1785c35b", "NzVlNDgzMjI3NmUwOGUyMDI5MDRhZWI5YzM1ZDU5ZGUwZDUzMDA1YTcwYzM0MzRkYTEyZTYzM2IyMjRiYjhhYg==");
			HttpResponse get = null; 
			get = client.Get("/api/account/endcert");
			String certres = IOUtils.toString(get.getEntity().getContent(),StandardCharsets.UTF_8); 

			String cert = "-----BEGIN CERTIFICATE-----\n"+
					certres+"\n"
					+"-----END CERTIFICATE-----";
			PublicKey pub = getpublickey(cert);

			Map<String,String> mapreq = new HashMap<String, String>();
            String datagoc = "123";
            mapreq.put("payload", DatatypeConverter.printBase64Binary(datagoc.getBytes()));
            mapreq.put("alg", "SHA1WithRSA");
            get = client.Post("/api/v2/bin/sign/base64",mapreq);
            
//            get = client.Post("/verify")
            String signature = IOUtils.toString(get.getEntity().getContent(),StandardCharsets.UTF_8); 
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> myMap =new Gson().fromJson(signature, type);
			String signbase = myMap.get("obj");
			verify(datagoc, signbase, pub);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static PublicKey getpublickey(String cert) {
		try {
			byte[] certBytes = cert.getBytes(java.nio.charset.StandardCharsets.UTF_8);

			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			InputStream in = new ByteArrayInputStream(certBytes);
			X509Certificate certificate = (X509Certificate)certFactory.generateCertificate(in);
			return certificate.getPublicKey();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	public static void verify(String datagoc,String signature,PublicKey pub) {
		try {
			Signature sig = Signature.getInstance("SHA1WithRSA");

			sig.initVerify(pub);
			sig.update(datagoc.getBytes());
			Boolean reponse = sig.verify(DatatypeConverter.parseBase64Binary(signature));
			System.out.println (reponse);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
