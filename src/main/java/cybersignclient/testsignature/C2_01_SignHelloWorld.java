package cybersignclient.testsignature;

import java.io.ByteArrayOutputStream;
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

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

public class C2_01_SignHelloWorld {

	public static String dest = "src/resource/signed1.pdf";
	public static String keystore = "src/resource/keystore/demo-rsa2048.ks";

	public static String src = "src/resource/1.pdf";

	public static char[] password = "demo-rsa2048".toCharArray();

	public static void sign(String src, String dest, Certificate[] chain, PrivateKey pk, String hashAlgorithm, String provider,
			CryptoStandard subfilter) throws FileNotFoundException, DocumentException, IOException, GeneralSecurityException {
		PdfReader reader = new PdfReader(src);
		FileOutputStream f = new FileOutputStream(dest);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper stp = PdfStamper.createSignature(reader, baos, '\0', null, true);
//		ByteArrayOutputStream baos = f;
		System.out.println("baos: " + baos.toString());
		

		// Create the signature appearance
		Rectangle rect = new Rectangle(36, 648, 200, 100);
		PdfSignatureAppearance sap = stp.getSignatureAppearance();

		sap.setReason("reason");
		sap.setLocation("location");
		sap.setVisibleSignature(rect, 1, "sig2");

		// provider = BC
		ExternalSignature pks = new PrivateKeySignature(pk, hashAlgorithm, provider);
		ExternalDigest digest = new BouncyCastleDigest();
		MakeSignature.signDetached(sap, digest, pks, chain, null, null, null, 0, subfilter);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, DocumentException, GeneralSecurityException {
		
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
		
		
		
		sign(src, dest, chain, pk, "SHA256", bcp.getName(), CryptoStandard.CMS);
		
	}

}
