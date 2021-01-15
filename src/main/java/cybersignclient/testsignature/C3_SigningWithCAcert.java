package cybersignclient.testsignature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CertificateUtil;
import com.itextpdf.text.pdf.security.CrlClient;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.OcspClient;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.TSAClient;

public class C3_SigningWithCAcert {

	public static String src = "src/resource/1.pdf";
	public static String dest = "src/resource/c3/1signed.pdf";
	
//	private void sign(String src, String dest, ) {
//		
//	}
	public void sign(String src, String dest,
			 Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider,
			 CryptoStandard subfilter, String reason, String location, Collection<CrlClient> crlList, OcspClient ocspClient, TSAClient tsaClient, int estimatedSize)
			 throws GeneralSecurityException, IOException, DocumentException {
			 // Creating the reader and the stamper
			 PdfReader reader = new PdfReader(src);
			 FileOutputStream os = new FileOutputStream(dest);
			 PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
			 // Creating the appearance
			 PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
			 appearance.setReason(reason);
			 appearance.setLocation(location);
			 appearance.setVisibleSignature(new Rectangle(36, 748, 144, 780), 1, "sig");
			 // Creating the signature
			 ExternalDigest digest = new BouncyCastleDigest();
			 ExternalSignature signature =
			 new PrivateKeySignature(pk, digestAlgorithm, provider);
			 MakeSignature.signDetached(appearance, digest, signature, chain,
			 null, null, null, 0, subfilter);
			}

	
	public static void main(String[] args) throws FileNotFoundException, IOException, GeneralSecurityException, DocumentException {
		
		// tao get pass private key
		Properties properties = new Properties();
		 properties.load(new FileInputStream("src/resource/properties/key.properties"));
		 String path = properties.getProperty("PRIVATE");
		 char[] pass = properties.getProperty("PASSWORD").toCharArray();
		 
		 BouncyCastleProvider provider = new BouncyCastleProvider();
		 Security.addProvider(provider);
		 KeyStore ks = KeyStore.getInstance("pkcs12", provider.getName());
		 ks.load(new FileInputStream(path), pass);
		 String alias = (String)ks.aliases().nextElement();
		 PrivateKey pk = (PrivateKey) ks.getKey(alias, pass);
		 Certificate[] chain = ks.getCertificateChain(alias);
		 
		 for(int i = 0; i < chain.length; i++) {
			 X509Certificate cert = (X509Certificate)chain[i];
			 System.out.println(String.format("[%s] %s", i, cert.getSubjectDN()));
			 System.out.println(CertificateUtil.getCRLURL(cert));
			 System.out.println("timestamp: " + CertificateUtil.getTSAURL(cert));
		 }
//		 SignWithCAcert app = new SignWithCAcert();
		 new C3_SigningWithCAcert().sign(src, dest, chain, pk, DigestAlgorithms.SHA256, provider.getName(),
		 CryptoStandard.CMS, "Test", "Ghent", null, null, null, 0);

	}
}
