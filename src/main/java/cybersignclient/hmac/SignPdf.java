package cybersignclient.hmac;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.google.gson.JsonSyntaxException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.security.DigestAlgorithms;

import cybersignclient.hmac.hashpdf.HashPdfFile;

public class SignPdf extends ClientCallApi implements Serializable {

	HttpResponse get = null;

	public SignPdf() throws KeyManagementException, MalformedURLException, KeyStoreException, NoSuchAlgorithmException,
			URISyntaxException {
		super();
		// TODO Auto-generated constructor stub
	}

	private String getSigned(String pathOriginalFile, String fileImage) throws IOException, KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, URISyntaxException {

		Map<String, String> mapreq = new HashMap<String, String>();
		byte[] b = FileUtils.readFileToByteArray(new File(pathOriginalFile));
		System.out.println("b: " + b.length);
		String b64 = DatatypeConverter.printBase64Binary(b);

		// chuyen du lieu file image ra base64
		byte[] byteImage = FileUtils.readFileToByteArray(new File(fileImage));
		String imageBase64 = Base64Utils.base64Encode(byteImage);
		PdfThuong pdfThuong = new PdfThuong();

		pdfThuong.base64pdf = b64;
		pdfThuong.hashalg = "SHA1";
		pdfThuong.typesignature = 1;
		pdfThuong.signaturename = "abc1";
		pdfThuong.base64image = imageBase64;
		pdfThuong.textout = "E:\\csharp\\bin\\Debug\\decode1.pdf";
		pdfThuong.pagesign = 1;
		pdfThuong.xpoint = 0;
		pdfThuong.ypoint = 0;
		pdfThuong.width = 100;
		pdfThuong.height = 100;

		// String json = JsonConvert.SerializeObject(pdfThuong);

		this.get = super.client.Post("/api/pdf/sign/originaldata", pdfThuong);
		System.out.println("get: " + get.toString());
		String theString = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
		System.out.println("fuckkkkk: " + theString);
		return theString;
	}

	private String verify(String base64signed) throws JsonSyntaxException, IOException {
		String objStr = getObject(base64signed, "base64pdfSigned");
		// verify
		this.get = client.Post("/api/pdf/verify", objStr);
		String theString = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
		
		return theString;
	}

	//
	
	
	
	public static void main(String[] args) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException,
			URISyntaxException, IOException {
		SignPdf signPdf = new SignPdf();
//		//sign
		String s = signPdf.getSigned("E:\\csharp\\bin\\Debug\\1.pdf", "E:\\csharp\\bin\\Debug\\1.png");
//		String s = signPdf.getSigned("E:\\csharp\\bin\\Debug\\decode.pdf", "E:\\csharp\\bin\\Debug\\1.png");
		System.out.println("fuck: " + s);
//		System.out.println(signPdf.getSigned("E:\\csharp\\bin\\Debug\\1.pdf", "E:\\csharp\\bin\\Debug\\1.png"));
		System.out.println("fuckkkkkk: " + signPdf.verify(s));
		
//		// write to file
		String signBase64 = signPdf.getObject(s, "base64pdfSigned");

		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] pdf = Base64Utils.base64Decode(signBase64);
		System.out.println("Baosssss: " + baos);				
		FileOutputStream fos = new FileOutputStream(new File("E:\\csharp\\bin\\Debug\\decode1.pdf"));						
		fos.write(pdf, 0, pdf.length);
		 fos.flush();
		 fos.close();
		
		String base64signed = FileUtils.readFileToString(new File("C:\\Users\\datnt\\Downloads\\pdfSignedResponse.json"));
//		String base64signed = Base64Utils.base64Encode(FileUtils.readFileToByteArray(new File("E:\\\\soap project\\\\testtool\\\\src\\\\main\\\\resources\\\\resulttest1.pdf")));
		System.out.println(signPdf.verify(base64signed));
		
		 
		 
	}

}
