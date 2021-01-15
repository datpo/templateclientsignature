package cybersignclient.hmac;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class SignXML extends ClientCallApi {

	HttpResponse get = null;

	public SignXML() throws KeyManagementException, MalformedURLException, KeyStoreException, NoSuchAlgorithmException,
			URISyntaxException {
		super();
		// TODO Auto-generated constructor stub
	}

	private String getSigned(String pathOriginalFile) throws IOException {

		Map<String, String> mapreq = new HashMap<String, String>();
		byte[] b = FileUtils.readFileToByteArray(new File(pathOriginalFile));
		String b64 = DatatypeConverter.printBase64Binary(b);

		mapreq.put("base64xml", b64);
		mapreq.put("hashalg", "SHA1");
		this.get = super.client.Post("/api/xml/sign/defaultdata", mapreq);
		String theString = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
		return theString;
	}

	private String verify(String base64signed) throws JsonSyntaxException, IOException {


		String objStr = getObject(base64signed, "base64xmlsigned");
		// verify
		this.get = client.Post("/api/xml/verify",objStr);
		String theString = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
		return theString;
	}

	public static void main(String[] args) throws KeyManagementException, MalformedURLException, KeyStoreException, NoSuchAlgorithmException, IOException, URISyntaxException {
		SignXML signXML = new SignXML();
		//get base64 office signed
		String base64 = signXML.getSigned("E:\\cyberhsm\\dev-test\\src\\resource\\test.xml");
		System.out.println(base64);
		// verify
		System.out.println(signXML.verify(base64));
	}
}
