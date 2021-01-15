package cybersignclient.hmac;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;

import com.cyber.provider.PKCS11JCE;

import CryptoServerAPI.CryptoServerConfig;
import CryptoServerAPI.CryptoServerException;
import CryptoServerCXI.CryptoServerCXI;
import CryptoServerCXI.CryptoServerCXI.Key;
import CryptoServerJCE.CryptoServerPrivateKey;
import CryptoServerJCE.CryptoServerProvider;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;



public class utimacotest {
	public static void main(String[] args) {
		/*PrivateKey pri;
		try {
			pri = loadPrivateKey();
			RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = priKey2KeySpace(pri);
			try {
				ImportKey(Long.parseLong("1"), "3602460987TEST1", rsaPrivateCrtKeySpec);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			DeleteObject("JCEADMIN","123456","0106234569");
			System.out.println("Xong");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	public static PrivateKey loadPrivateKey() 
	        throws IOException, GeneralSecurityException {
	    PrivateKey key = null;
	    InputStream is = null;
	    String strpri = "-----BEGIN RSA PRIVATE KEY-----\n" + 
	    		"MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCntotCnZgiVTxBCytPy8ESrpt98btMaDKaJbND9wC56AmJpex7pmJ7/FXTyNGyAEThinhqB2+jquKqodPqWtTscQTKz1cR3uWoprM1O3TZs2RzLlGjB5zvdHlN9eVo2j8+JtgoVIij8llTcyWgxZCns2Lq8L0QJAsbqD0yW33SntKm8UdevTN8i8zg/h8OZC2byK6aDSEWvgNP4wsmWpQY2IP1j5Whtfl2LtEAjv4+0nYzlK2AHKs60VrlAVyYDg/I1d6G8c85MVPXKfCdjc8nAI7M9W719rvfEZFEg6vR+S0jS2AausLX9yduJSPy7ACb9jLzAwR5cTdqfx6lGO7pAgMBAAECggEAFQMu8XqzO+H3+aI/m/DIUOHlg2MM92a6qEt+6U4CHkivITrTpS5UenpD+pPXifi0qJljus0RtKzNV1mNJniY4sLKOeq+WvPVTEsOeuXnHvt142RzP/zNMccjB5x+UKO/drcBMBRfRkMv49+yYDFAjXJRO2RMG8CYT6/FcxC5c/DUGELP14zJB4sPDjtN4twGjRXNLguAIyDpoY2pWil8OOmebVT0eDMvvSKb8BEjrsQN2quMtkiQJn4zBoXRmewptvdJYs9jFXsYDERuzODYzGobll484wiNnVzTaI9EyvolwjR9S6abvfaN+9mafnEaswuwu1LZjxUPKEPy1kjpgQKBgQDgOgmcRRMkjKRz+Nl6ohuk/sEVBrVxNafAbgV6bIdm9s/vPuFyd+FgsIgddUyZ2Nq5rNDyA+3b1OLprgw6nzjGd+uOI5oUTPjqJ+lspVhZbSmwo+0rOl9F779PvAK8dPmp5MqknI3S+PZALzqOBwl+P0HBVwVjgBzZ+NrfJiQNeQKBgQC/enBhGrRsJe6P1TUBw41sbOFeJ9oRAmIXx7q+KELnuBDFIlMqxBPDwY5Za1y2xdDumzxY0BrH0ft9Uf5ThXupbtCM3qhT6nAMa7zBqRdCPlD1p0a0nmVGjCGgPTA8dUGWIHiUVE6Qwuc27F7mG+k1QUma02gStlcx2O4S15pA8QKBgBsfCgCL3lfiZxXb+Lo7l2pqogIgEJxD6Cl/ZUw8ilgq7FW83loRJAKk96HQiVZmg3ZH7/IGBaQ8aMHpGnOQxLZE1QoZRk7ETKtN7o8EpN4UNSG3gj1jTxjPee10CUSwRW6GTPiCMylckbSLweJh+YT6s8Jl+zHg0hyPXDH3lA7JAoGAL3xw9FQIHK4HjNomTCqwHwydk5k0hnVd2GHrGQ+pEAhg+FK752Y7NJgC8oAivR0v5IzRGYF6ssWLphe4iAQZl/podBcgDbedVCc/55NEtmP39gQnD9IccbN8/11NYiD+Q/oHjVmfJl1/s+pI2q5fVpq7Y088hy97TlxrJncbxTECgYA6QSbjFnJEP/vpxowGq+iJiNbzU83XyJFyfHtEmZA1xk0b3yft2ZFC5vMJ+0UETttdGHriizArXEziRj/Q0Y3hta2B/B/ZaE7/f/6ySn8mdk0K7k8OULu8bhoymfPbkbaPr6G3oYkwPR6pzbm5W6lNpaLgMDjKabZkYVTiW+QMPg==\n" + 
	    		"-----END RSA PRIVATE KEY-----";
	    try {
	    	
	        is =  new ByteArrayInputStream(strpri.getBytes());
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        StringBuilder builder = new StringBuilder();
	        boolean inKey = false;
	        for (String line = br.readLine(); line != null; line = br.readLine()) {
	            if (!inKey) {
	                if (line.startsWith("-----BEGIN ") && 
	                        line.endsWith(" PRIVATE KEY-----")) {
	                    inKey = true;
	                }
	                continue;
	            }
	            else {
	                if (line.startsWith("-----END ") && 
	                        line.endsWith(" PRIVATE KEY-----")) {
	                    inKey = false;
	                    break;
	                }
	                builder.append(line);
	            }
	        }
	        //
	        byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
	        KeyFactory kf = KeyFactory.getInstance("RSA");
	        key = kf.generatePrivate(keySpec);
	    } finally {
	        closeSilent(is);
	    }
	    return key;
	}

	public static void closeSilent(final InputStream is) {
	    if (is == null) return;
	    try { is.close(); } catch (Exception ign) {}
	}
	public static RSAPrivateCrtKeySpec priKey2KeySpace(PrivateKey privateKey)
    {
        try
        {
            String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
            String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

            StringWriter stringWriter = new StringWriter();
            PEMWriter pemWriter = new PEMWriter(stringWriter);
            pemWriter.writeObject( privateKey);
            pemWriter.close();

            String privateKeyString = stringWriter.toString();
            privateKeyString = privateKeyString.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
            privateKeyString = privateKeyString.replaceAll("\\s", "");
            DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyString));

            DerValue[] seq = derReader.getSequence(0);

            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
            }
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger prime1 = seq[4].getBigInteger();
            BigInteger prime2 = seq[5].getBigInteger();
            BigInteger exp1 = seq[6].getBigInteger();
            BigInteger exp2 = seq[7].getBigInteger();
            BigInteger crtCoef = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);
            return keySpec;
        }
        catch (Exception ex)
        {

        }
        return null;
    }
	private static void initjce() {
		try {
			String[] objects = {"Device = 3009@10.30.1.26\n" + 
					"ConnectionTimeout = 1000\n" + 
					"Timeout = 1000\n" + 
					"KeepSessionAlive = 1"};
			 PKCS11JCE pkcs11JCE = new PKCS11JCE("Provider1", Arrays.asList(objects));
	        
	             String name = pkcs11JCE.getName();
//	             if(providerId.getId() == 1) {
//	                 Security.addProvider(pkcs11JCE);
//	             }
	         Security.addProvider(pkcs11JCE);
	         
	         KeyStore ks = KeyStore.getInstance("KeyStore", pkcs11JCE);

	         ks.load(null,null);
	        PrivateKey k = (PrivateKey) ks.getKey("0106234569", null);
	         ks.deleteEntry("0314560355");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private static void ImportKey(Long provider_id, String privateKeyname, RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec) throws  IOException, CryptoServerException, NoSuchAlgorithmException, InvalidKeySpecException, CryptoServerException {
//        Provider provider1 = providerRepository.findById(provider_id).orElseThrow(
//            () -> new BadRequestException("Provider not found " + provider_id)
//        );
        CryptoServerProvider provider = new CryptoServerProvider("Provider1");
        //List<DmKeystores> dmKeystores = dmKeystoresRepository.findByTrangthaiIsTrueAndDeletedOnIsNullAndProvider(provider1.getName());
        CryptoServerCXI.KeyAttributes localKeyAttributes = new CryptoServerCXI.KeyAttributes();
        localKeyAttributes.setName(privateKeyname);
        Set<String> devices_ls = new HashSet<>();
        String adminUser = null;
        String adminPass = null;
        //Khởi tạo mỗi cụm 1 provider
        InputStream inputStream = new ByteArrayInputStream(("Device = 3009@10.30.1.26\n" + 
        		"ConnectionTimeout = 1000\n" + 
        		"Timeout = 1000\n" + 
        		"KeepSessionAlive = 1\n"+
        		"SlotCount  = 200").getBytes(Charset.forName("UTF-8")));
        CryptoServerConfig cryptoserverconfig = null;
        cryptoserverconfig = new CryptoServerConfig(inputStream);
        String[] devices = cryptoserverconfig.getStringValues("Device", null);
        for (String device : devices) {
            devices_ls.add(device);
        }
        adminUser = "JCEADMIN";
        adminPass = "123456";

        String finalAdminUser = adminUser;
        String finalAdminPass = adminPass;
       
        
        for (String device : devices_ls) {
            CryptoServerCXI cs = null;
            try {
               
                cs = new CryptoServerCXI(device, 30000);
                cs.logonPassword(finalAdminUser, finalAdminPass);
                CryptoServerPrivateKey privateKey = engineGeneratePrivate(cs, provider, rsaPrivateCrtKeySpec);
                CryptoServerCXI.Key key = privateKey.getKey();
                key = cs.restoreKey(1, key, localKeyAttributes);
               
                cs.close();
               
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CryptoServerException e) {
                e.printStackTrace();
            } 
        }
    }
	public static boolean DeleteObject(String username, String password, String alias) throws Exception
	{
				
		//CryptoServerCXI provider = null;
	   
		//---------Login HSM------------
        System.out.println("Login Start!");
       
       // System.out.println("   File HSM config: " + path);
        String[] conf = {"Device = 3009@10.30.1.26\n" + 
				"ConnectionTimeout = 1000\n" + 
				"Timeout = 1000\n" + 
				"KeepSessionAlive = 1"};
        Set<String> devices_ls = new HashSet<>();
        InputStream stream = new ByteArrayInputStream(conf[0].getBytes(StandardCharsets.UTF_8)); 
        CryptoServerConfig cryptoserverconfig = null;
        cryptoserverconfig = new CryptoServerConfig(stream);
        String[] devices = cryptoserverconfig.getStringValues("Device", null);
        for (String device : devices) {
            devices_ls.add(device);
        }
        for (String device : devices_ls) {
        	 CryptoServerProvider provider = new CryptoServerProvider("Provider1");
            CryptoServerCXI cs = null;
            try {
               
                cs = new CryptoServerCXI(device, 30000);
                cs.logonPassword(username, password);
                CryptoServerCXI.KeyAttributes attr = new CryptoServerCXI.KeyAttributes(); 
                attr.setName("0106234569");
                Key kk = cs.findKey(attr);
                PKCS11JCE pkcs11JCE = new PKCS11JCE("Provider1", Arrays.asList(device));
               
                    String name = pkcs11JCE.getName();
//                    if(providerId.getId() == 1) {
//                        Security.addProvider(pkcs11JCE);
//                    }
                Security.addProvider(pkcs11JCE);
                KeyStore ks = KeyStore.getInstance("KeyStore", pkcs11JCE);
                ks.load(null, null);
//                System.out.println("   KeyStore: " + ks.getType());
//                ks.deleteEntry("111111111111111");
                Enumeration<String> kl = ks.aliases();
    	        
    	        
    	        while (kl.hasMoreElements())
    	        {
    	            String name1 = kl.nextElement();
    	            System.out.println("name1:"+name1);       
    	        }
                cs.close();
               
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CryptoServerException e) {
                e.printStackTrace();
            }
        }
	    return false;
                
	}
	protected static CryptoServerPrivateKey engineGeneratePrivate(CryptoServerCXI cs, CryptoServerProvider prov, KeySpec paramKeySpec) throws InvalidKeySpecException, IOException, CryptoServerException {
        CryptoServerCXI.KeyAttributes localKeyAttributes = new CryptoServerCXI.KeyAttributes();
        CryptoServerCXI.KeyComponents localKeyComponents = new CryptoServerCXI.KeyComponents();
        localKeyAttributes.setGenerationDate(new Date());
        CryptoServerCXI.Key localKey;
        localKeyAttributes.setAlgo(3);
        Object localObject1 = (RSAPrivateCrtKeySpec) paramKeySpec;
        localKeyAttributes.setExponent(((RSAPrivateCrtKeySpec) localObject1).getPublicExponent());
        localKeyAttributes.setSize(((RSAPrivateCrtKeySpec) localObject1).getModulus().bitLength());
        localKeyComponents.add(CryptoServerCXI.KeyComponents.TYPE_SEXP, ((RSAPrivateCrtKeySpec) localObject1).getPrivateExponent());
        localKeyComponents.add(CryptoServerCXI.KeyComponents.TYPE_P, ((RSAPrivateCrtKeySpec) localObject1).getPrimeP());
        localKeyComponents.add(CryptoServerCXI.KeyComponents.TYPE_Q, ((RSAPrivateCrtKeySpec) localObject1).getPrimeQ());
        localKeyComponents.add(CryptoServerCXI.KeyComponents.TYPE_DP, ((RSAPrivateCrtKeySpec) localObject1).getPrimeExponentP());
        localKeyComponents.add(CryptoServerCXI.KeyComponents.TYPE_DQ, ((RSAPrivateCrtKeySpec) localObject1).getPrimeExponentQ());
        localKeyComponents.add(CryptoServerCXI.KeyComponents.TYPE_U, ((RSAPrivateCrtKeySpec) localObject1).getCrtCoefficient());
        localKeyComponents.add(CryptoServerCXI.KeyComponents.TYPE_MOD, ((RSAPrivateCrtKeySpec) localObject1).getModulus());
        localKey = cs.importClearKey(3, 65536, localKeyAttributes, localKeyComponents);
        return new CryptoServerPrivateKey(prov, "RSA", localKey);
    }
}
