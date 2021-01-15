package cybersignclient.testsignature;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.ArrayList;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;

public class C5_VerifySinature {

	public void verifySignatures(String path) throws IOException, GeneralSecurityException {
		PdfReader reader = new PdfReader(path);
		AcroFields fields = reader.getAcroFields();
		ArrayList<String> names = fields.getSignatureNames();
		for (String sig : names) {
			System.out.println("=========" + sig + "==========");
			verifySignature(fields,sig);
		}
	}
	
	// check integrity of signature
	public void verifySignature(AcroFields fields, String name) throws GeneralSecurityException {
		System.out.println("Signature covers whole document: "
				 + fields.signatureCoversWholeDocument(name));
		System.out.println("Document revision: " + fields.getRevision(name)
		 + " of " + fields.getTotalRevisions());
		 PdfPKCS7 pkcs7 = fields.verifySignature(name);
		 
		 System.out.println("Integrity check OK? " + pkcs7.verify());
//		 return pkcs7;
	}
	
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		
		Security.addProvider(new BouncyCastleProvider());
		new C5_VerifySinature().verifySignatures("E:\\cyberhsm\\dev-test\\src\\resource\\d1.pdf");
	}
}
