package cybersignclient.hmac;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;

public class hashpdf {
	
    static ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// sap
	static PdfSignatureAppearance sap = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApiClient client = null;
		try {
			client = new ApiClient("http://localhost:8089");

			client.setCredentials("47dd2c92dfa246899d2c92dfa2868986", "MjhmYzdhZDkxZjI4ZWI2Y2FmNWIxYmEyMWRjNmMxMzM4NjI1Y2JmZDEzNjE2MWRhNWFkZmZkNjViNjAxYTk4Zg==");
			HttpResponse get = null;

			// get endcert
			get = client.Get("/api/account/endcert");
			
//			JSONObject jsonResult = new JsonObject(jsonString);
			
			String theString = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
			System.out.println(theString);
			// end get endcert

			Map<String, String> mapreq = new HashMap<String, String>();

			// tao du lieu 
			// kizHU/eUQGswjWRpOb2Rl7ffW2Y=
			// 2jmj7l5rSw0yVb/vlWAYkK/YBwk=

			// chuyen du lieu file image ra base64
			byte[] byteImage = FileUtils.readFileToByteArray(new File("E:\\csharp\\bin\\Debug\\1.png"));
			String imageBase64 = Base64Utils.base64Encode(byteImage);

			// pdf content
			byte[] pdfContent = FileUtils.readFileToByteArray(new File("E:\\csharp\\bin\\Debug\\1.pdf"));

			// baos
			//OutputStream baos = null;
			File file = new File("src/resource/signedpdf.pdf");
			
			String dest2 = "src/resource/dest2.pdf";
			

			hashpdf.HashPdfFile hashPdfFile = (new hashpdf()).new HashPdfFile();

			// cert
            
//			X509Certificate endCert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(
//					new ByteArrayInputStream(Base64Utils.base64Decode(get.ge.toString())));
			
			X509Certificate endCert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(
					new ByteArrayInputStream(Base64Utils.base64Decode(theString)));

//			byte[] bhashvalue = hashPdfFile(null, pdfContent, endCert, baos, sap, 1, 1, 1, 100, 100, 1, imageBase64,
//					"abc", "signaturename");
			
//			byte[] bhashvalue = hashPdfFile(null, pdfContent, endCert, hashPdfFile.baos, hashPdfFile.sap, 1, 1, 1, 100, 100, 1, imageBase64,
//			"abc", "signaturename");
			
			hashPdfFile = hashPdfFile(null, pdfContent, endCert, hashPdfFile.baos, hashPdfFile.sap, 1, 0, 0, 100, 100, 1, imageBase64,
					"abc", "signaturename");
			byte[] bhashvalue = hashPdfFile.hashpdfFile;
			
			System.out.println("signature: " + Base64Utils.base64Encode(bhashvalue));
			System.out.println("bao: " + hashPdfFile.baos.equals(null));
			System.out.println("sap: " + hashPdfFile.sap);
			
			// convert base 64
			String text = Base64Utils.base64Encode(bhashvalue);

			// end

			// ki so tren server
			mapreq.put("base64hash", text);
			mapreq.put("hashalg", "SHA1");
			get = client.Post("/api/pdf/sign/hashdata", mapreq);

			String theString1 = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
			System.out.println(theString1);
//	             System.out.println("decode: " + Base64Utils.base64Decode(theString));
	             
			System.out.println("Gia công");
			
			// add
//			Gson gson = new Gson();
//			String str = gson.fromJson(theString1, String.class);
//			System.out.println();
			
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(theString1).getAsJsonObject();
			String objStr = obj.get("obj").getAsString();
			System.out.println("string: " + objStr);
			
			// get signature type byte 
			// get hash string
			String[] s1 = theString1.split(":");
			String[] s2 = s1[4].split("\"");
			System.out.println("s2: " + s2[1]);
//			String re = "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";
			
//			byte[] extSignature = Base64Utils.base64Decode(re);
			
			byte[] extSignature = Base64Utils.base64Decode(s2[1]);
			
//			byte[] extSignature = s2[1].getBytes();
			
			
			
			addExternalSignature(extSignature, hashPdfFile.sap);
			// ghi mang to file
            
			byte[] pdf = hashPdfFile.baos.toByteArray();
			System.out.println("Baosssss: " + baos);
					
			FileOutputStream fos = new FileOutputStream(new File(dest2));
//			baos.writeTo(fos);
//			baos.flush();
			
			
			
			fos.write(pdf, 0, pdf.length);
			 fos.flush();
			 fos.close();
			
			System.out.println(baos.toString().getBytes());

			//
//			MakeSignature.signDetached(sap, bhashvalue, extSignature, endCert, null, null, null, 0, CryptoStandard.CMS);
			//
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addExternalSignature(byte[] extSignature, PdfSignatureAppearance sap)
    {
        try
        {
            byte[] paddedSig = new byte[8192];
           System.arraycopy(extSignature, 0, paddedSig, 0, extSignature.length);
            

            PdfDictionary dic2 = new PdfDictionary();
            dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
            
            sap.close(dic2);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
	
	public static hashpdf.HashPdfFile hashPdfFile(Font font, byte[] pfdContent, X509Certificate endCert, OutputStream baos,
			PdfSignatureAppearance sap, int page, int x, int y, int width, int height, int typeSig, String base64Image,
			String text, String signature_name) throws Exception, DocumentException {
//		baos = null;
//		sap = null;
		// create object
		hashpdf.HashPdfFile hashPdfFile = (new hashpdf()).new HashPdfFile();
		
		PdfReader reader = null;
		PdfStamper stp = null;

		reader = new PdfReader(pfdContent);
		baos = new ByteArrayOutputStream();
		
        
		stp = PdfStamper.createSignature(reader, baos, '\0', null, true);
		sap = stp.getSignatureAppearance();
		System.out.println("sap222: " + sap);
		System.out.println("Baos: " + baos.toString());
		Rectangle pageRect = new Rectangle((float) x, (float) y, (float) width, (float) height);
		sap.setVisibleSignature(pageRect, page, signature_name);
		sap.setCertificate(endCert);

		switch (typeSig) {
		case 1: {
			Image instance = Image.getInstance(DatatypeConverter.parseBase64Binary(base64Image));
			sap.setImage(instance);
			sap.setAcro6Layers(true);
			sap.setLayer2Text("");
			break;
		}
		case 2: {

			if (font == null) {
				BaseColor colorSign = new BaseColor(0, 128, 0);
				// var bytes = Resources.segoeui;
				byte[] fontfile = FileUtils.readFileToByteArray(new File("E:\\csharp\\Resources\\segoeui.ttf"));
				BaseFont bf = BaseFont.createFont("segoeui.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontfile,
						null);
				font = new Font(bf, 9, Font.NORMAL, colorSign);
			}
			sap.setLayer2Font(font);

			String noidung = "ky boi: " + text + "\n";
			noidung += "ky ngay" + new Date();

			sap.setLayer2Text(noidung);
			// sap.Layer2Text.PadLeft(100);
			break;
		}
		case 3: {
			if (font == null) {
				BaseColor colorSign = new BaseColor(0, 128, 0);
				byte[] fontfile = FileUtils.readFileToByteArray(new File("E:\\csharp\\Resources\\segoeui.ttf"));
				BaseFont bf = BaseFont.createFont("segoeui.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontfile,
						null);
				font = new Font(bf, 9, Font.NORMAL, colorSign);
			}
			sap.setLayer2Font(font);
			String noidung = "ky boi: " + text + "\n";
			noidung += "ky ngay" + new Date();

			Image instance2 = Image.getInstance(DatatypeConverter.parseBase64Binary(base64Image));
//                    instance2.setScalePercent(50);
//                    instance2.SetAbsolutePosition(100f, 150f);

			sap.setSignatureGraphic(instance2);
			/*
			 * DESCRIPTION = 0, NAME_AND_DESCRIPTION = 1, GRAPHIC_AND_DESCRIPTION = 2,
			 * GRAPHIC = 3
			 */
			// sap.setSignatureRenderingMode =
			// PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION;
			// sap.Image = instance2;
			// sap.Image.Alignment = 0;
			sap.setImageScale(0.3f);
			// sap.Image.ScaleAbsoluteHeight(height);
			new Rectangle((float) x, (float) y, (float) height, (float) height);
			sap.setAcro6Layers(false);
			sap.setLayer2Text(noidung);
			break;
		}
		default: {
			break;
		}
		}

		PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
		dic.setReason(sap.getReason());
		dic.setLocation(sap.getLocation());
		dic.setContact(sap.getContact());
		dic.setDate(new PdfDate(sap.getSignDate()));
		sap.setCryptoDictionary(dic);

		HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
		exc.put(PdfName.CONTENTS, (int) (8192 * 2 + 2));
		sap.preClose(exc);
		
		InputStream data = sap.getRangeStream();
		
		
		MessageDigest digest = MessageDigest.getInstance("SHA1");
//		data = new DigestInputStream(data, digest);
//		byte[] hash = digest.digest();
		
		byte hash[] = DigestAlgorithms.digest(data,digest);

		hashPdfFile.hashpdfFile = hash;
		hashPdfFile.baos = (ByteArrayOutputStream) baos;
		hashPdfFile.sap = sap;
		return hashPdfFile;
		//return hash;
		
	}
	
	class HashPdfFile{
		byte[] hashpdfFile;
		PdfSignatureAppearance sap;
		ByteArrayOutputStream baos;
	}

}
