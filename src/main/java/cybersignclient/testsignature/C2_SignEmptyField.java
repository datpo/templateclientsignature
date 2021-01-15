package cybersignclient.testsignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.SignatureEvent;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

public class C2_SignEmptyField {

	public static String dest = "src/resource/signedEmptyField.pdf";
	public static String keystore = "src/resource/keystore/demo-rsa2048.ks";

	public static String src1 = "src/resource/2.pdf";
	public static String src = "src/resource/1.pdf";
	public static char[] password = "demo-rsa2048".toCharArray();
	
	// create pdf
	public static void createPdf(String name) throws FileNotFoundException, DocumentException {
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(dest)));
		//open doc
		doc.open();
		// add content
		doc.add(new Paragraph("hello po"));
		// create a signature form field
		PdfFormField field = PdfFormField.createSignature(writer);
		field.setFieldName(name);
		
		 // set the widget properties
		 field.setPage(1);
		 field.setWidget(
		 new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_INVERT);
		 field.setFlags(PdfAnnotation.FLAGS_PRINT);
		 // add it as an annotation
		 writer.addAnnotation(field);
		 // maybe you want to define an appearance
		 PdfAppearance tp = PdfAppearance.createAppearance(writer, 72, 48);
		 tp.setColorStroke(BaseColor.BLUE);
		 tp.setColorFill(BaseColor.LIGHT_GRAY);
		 tp.rectangle(0.5f, 0.5f, 71.5f, 47.5f);
		 tp.fillStroke();
		 tp.setColorFill(BaseColor.BLUE);
		 ColumnText.showTextAligned(tp, Element.ALIGN_CENTER,
		 new Phrase("SIGN HERE"), 36, 24, 25);
		 field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
		 // step 5: Close the Document
		 doc.close();

	}
	
	public static void addSignatureField() throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		// create a signature form field
		PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
		field.setFieldName("signame");
		// set the widget properties
		field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_OUTLINE);
		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		// add the annotation
		stamper.addAnnotation(field, 1);
		// close the stamper
		stamper.close();
	}
	
	// add a empty
	
	// sign into field
	public static void sign(String src,String fieldName, String dest, Certificate[] chain, PrivateKey pk, String hashAlgorithm, String provider,
			CryptoStandard subfilter) throws FileNotFoundException, IOException, DocumentException, GeneralSecurityException {
		PdfReader reader = new PdfReader(new FileInputStream(new File(src)));
		PdfStamper stp = PdfStamper.createSignature(reader, new FileOutputStream(new File(dest)),'\0');
		

//		// create a signature form field
//		PdfFormField field = PdfFormField.createSignature(stp.getWriter());
//		field.setFieldName(fieldName);
//		// set the widget properties
//		field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_OUTLINE);
//		field.setFlags(PdfAnnotation.FLAGS_PRINT);
//		// add the annotation
//		stp.addAnnotation(field, 1);
//		// close the stamper
//		stp.close();

		System.out.println("stp: " + stp);
		//
		PdfSignatureAppearance sap = stp.getSignatureAppearance();
		
		System.out.println("sap: " + sap);
		sap.setReason("reason");
		sap.setLocation("location");
		
		sap.setVisibleSignature(fieldName);
		sap.setSignatureEvent(
				new SignatureEvent() {
					public void getSignatureDictionary(PdfDictionary sig) {
						sig.put(PdfName.NAME, new PdfString("fullname"));
					}
				}
				);
		sap.setCertificationLevel(1);
		sap.setVisibleSignature(
				new Rectangle(36, 700, 144, 732), 1, "Signature2");
				
		ExternalSignature pks = new PrivateKeySignature(pk, hashAlgorithm, provider);
		ExternalDigest digest = new BouncyCastleDigest();
		MakeSignature.signDetached(sap, digest, pks, chain, null, null, null, 0, subfilter);
		
	}
	
	public void addAnnotation(String src, String dest) {
		
	}
	
	public static void main(String[] args) throws DocumentException, IOException, GeneralSecurityException {
//		createPdf("signame");
//		addSignatureField();
		BouncyCastleProvider bcp = new BouncyCastleProvider();
        //Security.addProvider(bcp);
        Security.insertProviderAt(bcp, 1);
		
		// get key store
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(new File(keystore)), password);
        // get certificate
        
        String alias = ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
		Certificate[] chain = ks.getCertificateChain(alias);
		
		sign(dest,"signame", src1, chain, pk, "SHA256", bcp.getName(), CryptoStandard.CMS);
		
	}
}
