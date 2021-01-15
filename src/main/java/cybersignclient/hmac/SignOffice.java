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

public class SignOffice extends ClientCallApi {

	HttpResponse get = null;
	public SignOffice() throws KeyManagementException, MalformedURLException, KeyStoreException, NoSuchAlgorithmException,
			URISyntaxException {
		super();
		// TODO Auto-generated constructor stub

	}


	private String getSigned(String pathOriginalFile) throws IOException {
		
		Map<String, String> mapreq = new HashMap<String, String>();
		byte[] b = FileUtils.readFileToByteArray(new File(pathOriginalFile));
//		String b64 = DatatypeConverter.printBase64Binary(b);
		String b64 = Base64Utils.base64Encode(b);

		mapreq.put("base64office", b64);
		mapreq.put("hashalg", "SHA1");
		this.get = super.client.Post("/api/office/sign/originaldata", mapreq);
		String theString = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
		return theString;
	}
	

	
	private String verify(String base64OfficeSigned) throws JsonSyntaxException, IOException {

		String objStr = getObject(base64OfficeSigned);
		//objStr = "\""+objStr+"\"";
		// verify
		this.get = client.Post("/api/office/verify",objStr);
		String theString = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
		return theString;
	}
	
	public static void main(String[] args) throws KeyManagementException, MalformedURLException, KeyStoreException, NoSuchAlgorithmException, IOException, URISyntaxException {
		SignOffice signOffice = new SignOffice();
		//get base64 office signed
		String base64 = signOffice.getSigned("E:\\cyberhsm\\dev-test\\src\\resource\\result.docx");
//		String base64 = signXML.getSigned("C:\\Users\\datnt\\Downloads\\testsign.docx");
		System.out.println(base64);
//		signOffice.get = signOffice.client.Post("/api/office/verify",base64);
//		String theString = IOUtils.toString(signOffice.get.getEntity().getContent(), StandardCharsets.UTF_8);
//		System.out.println(theString);
		
		TestReadFile.readToFile(signOffice.getObject(base64, "base64officeSigned"), "E:\\cyberhsm\\dev-test\\src\\resource\\testSigned.docx" );
		
//		// verify file
//		byte[] b = FileUtils.readFileToByteArray(new File("C:\\Users\\datnt\\Downloads\\testsign.docx"));
//		 String b64 = DatatypeConverter.printBase64Binary(b);
//		String base64 = Base64Utils.base64Encode(b);
//		signXML.get = signXML.client.Post("/api/office/verify",base64);
//		String theString = IOUtils.toString(signXML.get.getEntity().getContent(), StandardCharsets.UTF_8);
//		System.out.println(theString);
		
		//verify
		System.out.println(signOffice.verify(base64));
	}

}
