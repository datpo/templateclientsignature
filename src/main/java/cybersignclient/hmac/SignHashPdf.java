package cybersignclient.hmac;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

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

import cybersignclient.hmac.hashpdf.HashPdfFile;

public class SignHashPdf{



	public static void main(String[] args) throws KeyManagementException, MalformedURLException, KeyStoreException, NoSuchAlgorithmException, URISyntaxException {
		// TODO Auto-generated method stub
		ApiClient client = new ClientCallApi().client;
		try {

			HttpResponse get = null;

			// get endcert
			get = client.Get("/api/account/endcert");
			
//			JSONObject jsonResult = new JsonObject(jsonString);
			
			String theString = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
			System.out.println(theString);
			// end get endcert

			Map<String, String> mapreq = new HashMap<String, String>();

			// chuyen du lieu file image ra base64
			byte[] byteImage = FileUtils.readFileToByteArray(new File("E:\\csharp\\bin\\Debug\\1.png"));
			String imageBase64 = Base64Utils.base64Encode(byteImage);

			// pdf content
//			byte[] pdfContent = FileUtils.readFileToByteArray(new File("E:\\csharp\\bin\\Debug\\1.pdf"));
			byte[] pdfContent = FileUtils.readFileToByteArray(new File("src/resource/dest2.pdf"));

			// baos
			//OutputStream baos = null;
			// path destination file
//			String dest2 = "src/resource/dest2.pdf";
			String dest2 = "src/resource/dest3.pdf";
			
			// init hash file pdf
			SignHashPdf.HashPdfFile hashPdfFile = (new SignHashPdf()).new HashPdfFile();

			// cert           
			
			X509Certificate endCert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(
					new ByteArrayInputStream(Base64Utils.base64Decode(theString)));

			//get 
//			hashPdfFile = hashPdfFile(null, pdfContent, endCert, hashPdfFile.baos, hashPdfFile.sap, 2, 0, 0, 100, 100, 1, imageBase64,
//					"abc", "signaturename");
			
			hashPdfFile = hashPdfFile(null, pdfContent, endCert, hashPdfFile.baos, hashPdfFile.sap, 2, 0, 0, 100, 100, 1, imageBase64,
					"abc", "signaturename1");
			byte[] bhashvalue = hashPdfFile.hashpdfFile;
			
			
			// convert base 64
			String text = Base64Utils.base64Encode(bhashvalue);

			// end

			// ki so tren server
			mapreq.put("base64hash", text);
			mapreq.put("hashalg", "SHA1");
			get = client.Post("/api/pdf/sign/hashdata", mapreq);

			String theString1 = IOUtils.toString(get.getEntity().getContent(), StandardCharsets.UTF_8);
			System.out.println(theString1);

	             
						
			
			// get signature type byte 
			// get hash string
			String[] s1 = theString1.split(":");
			String[] s2 = s1[4].split("\"");
			
			byte[] extSignature = Base64Utils.base64Decode(s2[1]);
			
//			byte[] extSignature = s2[1].getBytes();
			
			
			
			addExternalSignature(extSignature, hashPdfFile.sap);
			// ghi mang to file
            
			byte[] pdf = hashPdfFile.baos.toByteArray();

					
			FileOutputStream fos = new FileOutputStream(new File(dest2));						
			
			fos.write(pdf, 0, pdf.length);
			 fos.flush();
			 fos.close();			

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
	
	public static SignHashPdf.HashPdfFile hashPdfFile(Font font, byte[] pfdContent, X509Certificate endCert, OutputStream baos,
			PdfSignatureAppearance sap, int page, int x, int y, int width, int height, int typeSig, String base64Image,
			String text, String signature_name) throws Exception, DocumentException {
//		baos = null;
//		sap = null;
		// create object
		SignHashPdf.HashPdfFile hashPdfFile = (new SignHashPdf()).new HashPdfFile();
		
		PdfReader reader = null;
		PdfStamper stp = null;

		reader = new PdfReader(pfdContent);
		baos = new ByteArrayOutputStream();
		
        
		stp = PdfStamper.createSignature(reader, baos, '\0', null, true);
		sap = stp.getSignatureAppearance();
		System.out.println("fuckkkkkkkkkkk: " + sap);

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
