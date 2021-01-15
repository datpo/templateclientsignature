package cybersignclient.testsignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

public class C2_SignMultiTime {

	public static String dest = "src/resource/signedEmptyField.pdf";
	public static String keystore = "src/resource/keystore/demo-rsa2048.ks";
	public static final String ALICE = "src/resource/keystore/alice.ks";
	public static final String BOB = "src/resource/keystore/bob.ks";
	public static final String CAROL = "src/resource/keystore/carol.ks";
	
	public static String d1 = "src/resource/d1.pdf";
	public static String d2 = "src/resource/d2.pdf";
	public static String d3 = "src/resource/d3.pdf";
	
	

//	public static String src1 = "src/resource/2.pdf";
	public static String src = "src/resource/1.pdf";
//	public static char[] password = "demo-rsa2048".toCharArray();
	public static char[] password = "123456".toCharArray();

	public void createForm() throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(src));
		document.open();
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		table.addCell("Signer 1: Alice");
		table.addCell(createSignatureFieldCell(writer, "sig1"));
		table.addCell("Signer 2: Bob");
		table.addCell(createSignatureFieldCell(writer, "sig2"));
		table.addCell("Signer 3: Carol");
		table.addCell(createSignatureFieldCell(writer, "sig3"));
		document.add(table);
		document.close();
	}

	protected PdfPCell createSignatureFieldCell(PdfWriter writer, String name) {
		PdfPCell cell = new PdfPCell();
		cell.setMinimumHeight(50);
		PdfFormField field = PdfFormField.createSignature(writer);
		field.setFieldName(name);
		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		cell.setCellEvent(new MySignatureFieldEvent(field));
		return cell;
	}

	public class MySignatureFieldEvent implements PdfPCellEvent {
		public PdfFormField field;

		public MySignatureFieldEvent(PdfFormField field) {
			this.field = field;
		}

		public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
			PdfWriter writer = canvases[0].getPdfWriter();
			field.setPage();
			field.setWidget(position, PdfAnnotation.HIGHLIGHT_INVERT);
			writer.addAnnotation(field);
		}
	}

	public void sign(String keystore, int level, String src, String name, String dest)
			throws GeneralSecurityException, IOException, DocumentException {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream(keystore), password);
		String alias = (String) ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
		Certificate[] chain = ks.getCertificateChain(alias);
		// Creating the reader and the stamper
		PdfReader reader = new PdfReader(src);
		FileOutputStream os = new FileOutputStream(dest);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
		// Creating the appearance
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setVisibleSignature(name);
		appearance.setCertificationLevel(level);
		// Creating the signature
		ExternalSignature pks = new PrivateKeySignature(pk, "SHA-256", "BC");
		ExternalDigest digest = new BouncyCastleDigest();
		MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0, CryptoStandard.CMS);
	}
	
	public void fillOut(String src, String dest, String name, String value) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stp = new PdfStamper(reader, new FileOutputStream(new File(dest)), '\0', true);
		AcroFields form = stp.getAcroFields();
		form.setField(name, value);
		form.setFieldProperty(name, "setfflags", PdfFormField.FF_READ_ONLY, null);
		stp.close();
	}

	public static void main(String[] args) throws IOException, DocumentException, GeneralSecurityException {
		C2_SignMultiTime smt = new C2_SignMultiTime();

		smt.createForm();
//		smt.createSignatureFieldCell(writer, name)
		BouncyCastleProvider bcp = new BouncyCastleProvider();
        //Security.addProvider(bcp);
        Security.insertProviderAt(bcp, 1);

//		smt.sign(ALICE, PdfSignatureAppearance.CERTIFIED_FORM_FILLING, src, "sig1", String.format(dest, "alice"));
//		smt.sign(BOB, PdfSignatureAppearance.NOT_CERTIFIED, dest, "sig2",
//				d1);
//		smt.sign(CAROL, PdfSignatureAppearance.NOT_CERTIFIED, String.format(dest, "bob"), "sig3",
//				String.format(dest, "carol"));
		
		// 
        smt.fillOut(d1, d2, "sig2", "abc");
	}

}
