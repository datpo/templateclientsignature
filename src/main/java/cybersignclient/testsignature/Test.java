package cybersignclient.testsignature;

import org.apache.commons.codec.Charsets;
import org.bouncycastle.util.encoders.Base64;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;

import cybersignclient.hmac.Base64Utils;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class Test {

	    public static final String CERT = "MIIEnDCCA4SgAwIBAgIQVAEBCR8Rug4iBWInHTwAAjANBgkqhkiG9w0BAQUFADBfMSYwJAYDVQQDDB1Mb3R1c0NBIERpZ2l0YWwgU2lnbmF0dXJlIFNKQzEoMCYGA1UECgwfQ8OUTkcgVFkgQ+G7lCBQSOG6pk4gQ1lCRVJMT1RVUzELMAkGA1UEBhMCVk4wHhcNMjAwNTEyMDkwMjQ3WhcNMjEwNTEzMDkwMjQ3WjA/MR4wHAYKCZImiZPyLGQBARMOTVNUOjAxMjM0NTY3ODkxEDAOBgNVBAMTB0JGREZHREcxCzAJBgNVBAYTAlZOMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq/bIm239/CtcrRSIivyTGaA0/BG7uCPp+YfdyO9U/dNbHhfttgPksuzqyWUv+eaWeVddpiqyMhcT/WUvGfJbQ77qMZLI/VCFoJjRbq9ziDax7hDlaWwXGCVhL5jOyyncvDZhVickLW6ihkuaLMrBId/TpE5sXV/LbQdbrzTbBwuUXEsr1V3eHh9CxHsmyiCgZ/AKJiy4AKH4ZTgUPTuQ9dnv+AqdSHovAHNUsTLM8E+YHkHaIG8+rwo3T7+2ikiCw8/APj6YPQWzwQhKkoDQbL1lt+sgpTjIk4QSo7RqgtiRm+h6sJKePfbZn/5rsxlASRd0pnKUYwSG1ltuByfzkQIDAQABo4IBcjCCAW4wDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAWgBShLzDDL1rfUtaWcnvIZyXiD1bFJDA3BggrBgEFBQcBAQQrMCkwJwYIKwYBBQUHMAGGG2h0dHA6Ly9kZW1vb2NzcC5sb3R1c2NhLmNvbTASBgNVHREECzAJgQdzdmRAZmhqMDoGA1UdIAQzMDEwLwYMKwYBBAGB7QMBCQMBMB8wHQYIKwYBBQUHAgIwEQwPT1NfSFNNX0N5YmVyXzFZMDQGA1UdJQQtMCsGCCsGAQUFBwMCBggrBgEFBQcDBAYKKwYBBAGCNwoDDAYJKoZIhvcvAQEFME8GA1UdHwRIMEYwRKAqoCiGJmh0dHA6Ly9kZW1vY3JsLmxvdHVzY2EuY29tL2xvdHVzY2EuY3JsohakFDASMRAwDgYDVQQDDAdMT1RVU0NBMB0GA1UdDgQWBBQVt+z+9kbnVD2xKfhgaRnIalk+iDAOBgNVHQ8BAf8EBAMCBPAwDQYJKoZIhvcNAQEFBQADggEBAADUhjWxFeBSPpsyvP/AZgtNcHfmjBiHZhKbgzGkTwRMy5bOtfdP17Zj2s5pVshlUtkKKgXArf5PSbPFNhjuf9nMT95q5ZirE40ljfKXRg9xJswcshV+gVeEEqA+2a9RLrusy0YkzERQXFT0wPr5F3y+FGMKSVUO9vZHu0ZATdwlV/VOQmlnivuOR/crmjzBKu50xeK2ZglpYOYzSqqdm5y/ekQTN2S6oLG4Br3JU3bnoaGSg+mJUVkQ0SRY5CR9JnqZt5V4J1w3iUMGVTLIM+kl8J4QqrhvUXcEXMkP9OpTar17ti5pLvSKGtO3N3prAO0PA0jt9/PBGvktFKVjyFo=";
	    public static final String SRC = "E:\\csharp\\bin\\Debug\\1.pdf";
	    public static final String DEST = "src/resources/signed.pdf";

	    public static void main(String args[]) throws IOException {
	        getHash(SRC, CERT);
	    }



	    public static void getHash(String doc, String cert) throws IOException {

	        try {

//	            File initialFile = new File(cert);
//	            InputStream is = new FileInputStream(initialFile);

	            // We get the self-signed certificate from the client
	            CertificateFactory factory = CertificateFactory.getInstance("X.509");
	            Certificate[] chain = new Certificate[1];
//	            chain[0] = factory.generateCertificate(is);
	            chain[0] = factory.generateCertificate(new ByteArrayInputStream(Base64Utils.base64Decode(cert)));

	            // we create a reader and a stamper
	            PdfReader reader = new PdfReader(doc);
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            PdfStamper stamper = PdfStamper.createSignature(reader, baos, '\0');

	            // we create the signature appearance
	            PdfSignatureAppearance sap = stamper.getSignatureAppearance();
	            sap.setReason("TEST REASON");
	            sap.setLocation("TEST LOCATION");
	            //sap.setVisibleSignature(new Rectangle(36, 748, 144, 780), 1, "sig"); //visible
	            sap.setVisibleSignature(new Rectangle(36, 748, 36, 748), 1, "sig"); //invisible
	            sap.setCertificate(chain[0]);

	            // we create the signature infrastructure
	            PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
	            dic.setReason(sap.getReason());
	            dic.setLocation(sap.getLocation());
	            dic.setContact(sap.getContact());
	            dic.setDate(new PdfDate(sap.getSignDate()));
	            sap.setCryptoDictionary(dic);
	            HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
	            exc.put(PdfName.CONTENTS, new Integer(8192 * 2 + 2));
	            sap.preClose(exc);
	            ExternalDigest externalDigest = new ExternalDigest() {
	                public MessageDigest getMessageDigest(String hashAlgorithm)
	                        throws GeneralSecurityException {
	                    return DigestAlgorithms.getMessageDigest(hashAlgorithm, null);
	                }
	            };
	            PdfPKCS7 sgn = new PdfPKCS7(null, chain, "SHA256", null, externalDigest, false);
	            InputStream data = sap.getRangeStream();
	            byte hash[] = DigestAlgorithms.digest(data, externalDigest.getMessageDigest("SHA256"));


//	            // we get OCSP and CRL for the cert
//	            OCSPVerifier ocspVerifier = new OCSPVerifier(null, null);
//	            OcspClient ocspClient = new OcspClientBouncyCastle(ocspVerifier);
//	            byte[] ocsp = null;
//	            if (chain.length >= 2 && ocspClient != null) {
//	                ocsp = ocspClient.getEncoded((X509Certificate) chain[0], (X509Certificate) chain[1], null);
//	            }

	        byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, null, null, null, MakeSignature.CryptoStandard.CMS);
	        InputStream sh_is = new ByteArrayInputStream(sh);
	        byte[] signedAttributesHash = DigestAlgorithms.digest(sh_is, externalDigest.getMessageDigest("SHA256"));


	        System.out.println("----------------------------------------------");
	        System.out.println("Hash to be sign:");
	        System.out.println( new String(Base64.encode(signedAttributesHash), Charsets.UTF_8));
	            System.out.println("----------------------------------------------");
	            System.out.println("Insert b64 signed hash [ENTER]");
	            System.out.println("----------------------------------------------");

	            Scanner in = new Scanner(System.in);
	            String signedHashB64 = in.nextLine();
	            System.out.println( signedHashB64);

	            ByteArrayOutputStream os = baos;

	            byte[] signedHash = org.apache.commons.codec.binary.Base64.decodeBase64(signedHashB64.getBytes());

	            // we complete the PDF signing process
	            sgn.setExternalDigest(signedHash, null, "RSA");
	            Collection<byte[]> crlBytes = null;
	            TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle("http://timestamp.gdca.com.cn/tsa", null, null);

//	            byte[] encodedSig = sgn.getEncodedPKCS7(hash, tsaClient, ocsp, crlBytes, MakeSignature.CryptoStandard.CMS);
	            byte[] encodedSig = sgn.getEncodedPKCS7(hash, null, tsaClient, null, crlBytes, MakeSignature.CryptoStandard.CMS);
	            byte[] paddedSig = new byte[8192];
	            System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
	            PdfDictionary dic2 = new PdfDictionary();
	            dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));

	            try {
	                sap.close(dic2);
	            } catch (DocumentException e) {
	                throw new IOException(e);
	            }

	            FileOutputStream fos = new FileOutputStream(new File(DEST));
	            os.writeTo(fos);

	            System.out.println("pdfsig " + System.getProperty("user.dir") + "/" + DEST);
	            System.out.println("------------------End Of Life --------------------------");

	            System.exit(0);


	        } catch (GeneralSecurityException e) {
	            throw new IOException(e);
	        } catch (DocumentException e) {
	            throw new IOException(e);
	        }
	        
	        String datpoiloveu = "asdasd";
	        datpoiloveu = "1234";

	    }


	
}
