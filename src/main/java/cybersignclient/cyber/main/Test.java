package cybersignclient.cyber.main;

//

//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cybersignclient.cyber.ooxml.OOXMLSigner;
import cybersignclient.hmac.ApiClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

public class Test {
	public Test() {
	}

	public static void main(String[] args) {
		signFileOffice();
	}

	public static void signFileOffice() {
		try {
			ApiClient client = null;
			HttpResponse get = null;
			client = new ApiClient("https://api.cyberhsm.vn");
			client.setCredentials("2ca759fc5c4f4830a759fc5c4fd830cf",
					"YmQyMjlhNzZjY2FkNDEzNjI5Yjk2ZTg3MTFlZTEzZGI5MjYxZWUzMmZkMWUwNTczZDZlODMwYzlhOGFmYjgxYQ==");
			get = client.Get("/api/account/endcert");
			String certRes = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
			X509Certificate x509Cert = convertToX509Cert(certRes);
			X509Certificate[] chain = new X509Certificate[] { x509Cert };
			byte[] byteTKhai = readFileInByteArray("E:\\csharp\\bin\\Debug\\test.docx");
			OOXMLSigner ooxmlSigner = new OOXMLSigner();
			byte[] signed = ooxmlSigner.signOOXMLFile(byteTKhai, (PrivateKey) null, chain, (Provider) null);
			System.out.println("truoc khi ky chua hash : " + new String(signed, "UTF-8"));
			Map<String, String> mapreq = new HashMap();
			mapreq.put("payload", DatatypeConverter.printBase64Binary(signed));
			mapreq.put("alg", "SHA1WithRSA");
			get = client.Post("/api/bin/sign/base64", mapreq);
			String signature = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
			System.out.println(signature);
			Type type = (new TypeToken<Map<String, String>>() {
			}).getType();
			Map<String, String> myMap = (Map) (new Gson()).fromJson(signature, type);
			String signbase = (String) myMap.get("obj");
			byte[] signedFileContent = ooxmlSigner.signOOXMLFile(DatatypeConverter.parseBase64Binary(signbase), chain);
			FileUtils.writeByteArrayToFile(new File("E:\\csharp\\bin\\Debug\\testsign.docx"), signedFileContent);
		} catch (Exception var15) {
			var15.printStackTrace();
		}

		System.out.println(">>>>> Finish <<<<<");
	}

	public static X509Certificate convertToX509Cert(String certificateString) throws CertificateException {
		X509Certificate certificate = null;
		CertificateFactory cf = null;

		try {
			if (certificateString != null && !certificateString.trim().isEmpty()) {
				certificateString = certificateString.replace("-----BEGIN CERTIFICATE-----\n", "")
						.replace("-----END CERTIFICATE-----", "");
				byte[] certificateData = DatatypeConverter.parseBase64Binary(certificateString);
				cf = CertificateFactory.getInstance("X509");
				certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateData));
			}

			return certificate;
		} catch (CertificateException var4) {
			throw new CertificateException(var4);
		}
	}

	public static byte[] readFileInByteArray(String aFileName) throws IOException {
		File file = new File(aFileName);
		FileInputStream fileStream = new FileInputStream(file);

		try {
			int fileSize = (int) file.length();
			byte[] data = new byte[fileSize];

			for (int bytesRead = 0; bytesRead < fileSize; bytesRead += fileStream.read(data, bytesRead,
					fileSize - bytesRead)) {
			}

			byte[] var8 = data;
			return var8;
		} finally {
			fileStream.close();
		}
	}
}
