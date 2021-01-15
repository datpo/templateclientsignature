package cybersignclient.hmac;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class maintestnd2 {

	public static void main(String[] args) throws CertificateException {
		// TODO Auto-generated method stub
		ApiClient client = null;
		try {
			client = new ApiClient("http://localhost:8089");
			client.setCredentials("7e41c5dfe91b4b6c81c5dfe91b3b6c8a", "Nzc4MWFlYjU1NjMyNTg5ZWQ5ZTNmNDdiNmZmY2E4NGJjNDE4NWExODQwMjYyODI0NzU3MTUzMzM4OGE5M2Q2NA==");
			HttpResponse get = null; 
			get = client.Get("/api/account/endcert");
			String certres = IOUtils.toString(get.getEntity().getContent(),StandardCharsets.UTF_8); 

			String cert = "-----BEGIN CERTIFICATE-----\n"+
					certres+"\n"
					+"-----END CERTIFICATE-----";
			PublicKey pub = getpublickey(cert);

			Map<String,String> mapreq = new HashMap<String, String>();
            String datagoc = "{\r\n" + 
            		"    \"NGAY1_M\": {\r\n" + 
            		"        \"ngay_n1\": 20200827,\r\n" + 
            		"        \"ma_daithe\": \"DT\",\r\n" + 
            		"        \"ten_daithe\": \"Dịch trong\",\r\n" + 
            		"        \"daithe_khac\": \"\",\r\n" + 
            		"        \"lymphocytes\": \"\",\r\n" + 
            		"        \"neutrophils\": \"85\",\r\n" + 
            		"        \"soluongtbbc\": \"16\",\r\n" + 
            		"        \"ma_nguoithuchien_ngay1\": \"NHN015\",\r\n" + 
            		"        \"ten_nguoithuchien_ngay1\": \"Nguyễn Trọng Nhân\",\r\n" + 
            		"        \"ten_nguoithuchien_cayphanlap\": \"Nguyễn Trọng Nhân\",\r\n" + 
            		"        \"ma_ten_nguoithuchien_cayphanlap\": \"NHN015\"\r\n" + 
            		"    },\r\n" + 
            		"    \"LOAITEBAO\": [\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"BM\",\r\n" + 
            		"            \"ten\": \"TB biểu mô\",\r\n" + 
            		"            \"sotb\": null,\r\n" + 
            		"            \"bandinhluong\": null\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"DAN\",\r\n" + 
            		"            \"ten\": \"BC đa nhân\",\r\n" + 
            		"            \"sotb\": null,\r\n" + 
            		"            \"bandinhluong\": \"(-)\"\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"DON\",\r\n" + 
            		"            \"ten\": \"BC đơn nhân\",\r\n" + 
            		"            \"sotb\": null,\r\n" + 
            		"            \"bandinhluong\": \"(-)\"\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"TRU\",\r\n" + 
            		"            \"ten\": \"TB trụ\",\r\n" + 
            		"            \"sotb\": null,\r\n" + 
            		"            \"bandinhluong\": \"\"\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"HC\",\r\n" + 
            		"            \"ten\": \"Hồng cầu\",\r\n" + 
            		"            \"sotb\": null,\r\n" + 
            		"            \"bandinhluong\": \"\"\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"KHAC\",\r\n" + 
            		"            \"ten\": \"Khác\",\r\n" + 
            		"            \"sotb\": null,\r\n" + 
            		"            \"bandinhluong\": null\r\n" + 
            		"        }\r\n" + 
            		"    ],\r\n" + 
            		"    \"CAYPHANLAP\": [\r\n" + 
            		"        {\r\n" + 
            		"            \"ba\": \"False\",\r\n" + 
            		"            \"ca\": \"False\",\r\n" + 
            		"            \"ma\": \"BD\",\r\n" + 
            		"            \"mc\": \"False\",\r\n" + 
            		"            \"ss\": \"False\",\r\n" + 
            		"            \"sda\": \"False\",\r\n" + 
            		"            \"ten\": \"35-37C (Binder BD-115/MI-ba-006)\",\r\n" + 
            		"            \"khac\": null\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ba\": \"True\",\r\n" + 
            		"            \"ca\": \"True\",\r\n" + 
            		"            \"ma\": \"CO2\",\r\n" + 
            		"            \"mc\": \"False\",\r\n" + 
            		"            \"ss\": \"False\",\r\n" + 
            		"            \"sda\": \"False\",\r\n" + 
            		"            \"ten\": \"35-37C, 5% CO2 (Advantage-Lab/MI-vr-005)\",\r\n" + 
            		"            \"khac\": null\r\n" + 
            		"        }\r\n" + 
            		"    ],\r\n" + 
            		"    \"LOAIVIKHUAN\": [\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"CK\",\r\n" + 
            		"            \"gra\": null,\r\n" + 
            		"            \"grd\": \"(-)\",\r\n" + 
            		"            \"ten\": \"Cầu khuẩn\",\r\n" + 
            		"            \"sovikhuan\": null,\r\n" + 
            		"            \"cachsapxep\": \"\"\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"TK\",\r\n" + 
            		"            \"gra\": \"(-)\",\r\n" + 
            		"            \"grd\": null,\r\n" + 
            		"            \"ten\": \"Trực khuẩn\",\r\n" + 
            		"            \"sovikhuan\": null,\r\n" + 
            		"            \"cachsapxep\": null\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"CTK\",\r\n" + 
            		"            \"gra\": \"(-)\",\r\n" + 
            		"            \"grd\": null,\r\n" + 
            		"            \"ten\": \"Cầu trực khuẩn\",\r\n" + 
            		"            \"sovikhuan\": null,\r\n" + 
            		"            \"cachsapxep\": null\r\n" + 
            		"        },\r\n" + 
            		"        {\r\n" + 
            		"            \"ma\": \"KHAC\",\r\n" + 
            		"            \"gra\": null,\r\n" + 
            		"            \"grd\": null,\r\n" + 
            		"            \"ten\": \"Khác\",\r\n" + 
            		"            \"sovikhuan\": null,\r\n" + 
            		"            \"cachsapxep\": null\r\n" + 
            		"        }\r\n" + 
            		"    ]\r\n" + 
            		"}";
            System.out.println(DatatypeConverter.printBase64Binary(datagoc.getBytes()));
            mapreq.put("payload", DatatypeConverter.printBase64Binary(datagoc.getBytes()));
            mapreq.put("alg", "SHA1WithRSA");
            System.out.println(new Gson().toJson(mapreq));
            get = client.Post("/api/v2/json/sign/hash",mapreq);
            String signature = IOUtils.toString(get.getEntity().getContent(),StandardCharsets.UTF_8); 
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> myMap =new Gson().fromJson(signature, type);
			String signbase = myMap.get("obj");
			//verify(datagoc, signbase, pub);
			
			 String datagocve = "{\r\n" + 
	            		"    \"NGAY1_M\": {\r\n" + 
	            		"        \"ngay_n1\": 20200827,\r\n" + 
	            		"        \"ma_daithe\": \"DT\",\r\n" + 
	            		"        \"ten_daithe\": \"Dịch trong\",\r\n" + 
	            		"        \"daithe_khac\": \"\",\r\n" + 
	            		"        \"lymphocytes\": \"\",\r\n" + 
	            		"        \"neutrophils\": \"85\",\r\n" + 
	            		"        \"soluongtbbc\": \"16\",\r\n" + 
	            		"        \"ma_nguoithuchien_ngay1\": \"NHN015\",\r\n" + 
	            		"        \"ten_nguoithuchien_ngay1\": \"Nguyễn Trọng Nhân\",\r\n" + 
	            		"        \"ten_nguoithuchien_cayphanlap\": \"Nguyễn Trọng Nhân\",\r\n" + 
	            		"        \"ma_ten_nguoithuchien_cayphanlap\": \"NHN015\"\r\n" + 
	            		"    },\r\n" + 
	            		"    \"LOAITEBAO\": [\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"BM\",\r\n" + 
	            		"            \"ten\": \"TB biểu mô\",\r\n" + 
	            		"            \"sotb\": null,\r\n" + 
	            		"            \"bandinhluong\": null\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"DAN\",\r\n" + 
	            		"            \"ten\": \"BC đa nhân\",\r\n" + 
	            		"            \"sotb\": null,\r\n" + 
	            		"            \"bandinhluong\": \"(-)\"\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"DON\",\r\n" + 
	            		"            \"ten\": \"BC đơn nhân\",\r\n" + 
	            		"            \"sotb\": null,\r\n" + 
	            		"            \"bandinhluong\": \"(-)\"\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"TRU\",\r\n" + 
	            		"            \"ten\": \"TB trụ\",\r\n" + 
	            		"            \"sotb\": null,\r\n" + 
	            		"            \"bandinhluong\": \"\"\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"HC\",\r\n" + 
	            		"            \"ten\": \"Hồng cầu\",\r\n" + 
	            		"            \"sotb\": null,\r\n" + 
	            		"            \"bandinhluong\": \"\"\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"KHAC\",\r\n" + 
	            		"            \"ten\": \"Khác\",\r\n" + 
	            		"            \"sotb\": null,\r\n" + 
	            		"            \"bandinhluong\": null\r\n" + 
	            		"        }\r\n" + 
	            		"    ],\r\n" + 
	            		"    \"CAYPHANLAP\": [\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ba\": \"False\",\r\n" + 
	            		"            \"ca\": \"False\",\r\n" + 
	            		"            \"ma\": \"BD\",\r\n" + 
	            		"            \"mc\": \"False\",\r\n" + 
	            		"            \"ss\": \"False\",\r\n" + 
	            		"            \"sda\": \"False\",\r\n" + 
	            		"            \"ten\": \"35-37C (Binder BD-115/MI-ba-006)\",\r\n" + 
	            		"            \"khac\": null\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ba\": \"True\",\r\n" + 
	            		"            \"ca\": \"True\",\r\n" + 
	            		"            \"ma\": \"CO2\",\r\n" + 
	            		"            \"mc\": \"False\",\r\n" + 
	            		"            \"ss\": \"False\",\r\n" + 
	            		"            \"sda\": \"False\",\r\n" + 
	            		"            \"ten\": \"35-37C, 5% CO2 (Advantage-Lab/MI-vr-005)\",\r\n" + 
	            		"            \"khac\": null\r\n" + 
	            		"        }\r\n" + 
	            		"    ],\r\n" + 
	            		"    \"LOAIVIKHUAN\": [\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"CK\",\r\n" + 
	            		"            \"gra\": null,\r\n" + 
	            		"            \"grd\": \"(-)\",\r\n" + 
	            		"            \"ten\": \"Cầu khuẩn\",\r\n" + 
	            		"            \"sovikhuan\": null,\r\n" + 
	            		"            \"cachsapxep\": \"\"\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"TK\",\r\n" + 
	            		"            \"gra\": \"(-)\",\r\n" + 
	            		"            \"grd\": null,\r\n" + 
	            		"            \"ten\": \"Trực khuẩn\",\r\n" + 
	            		"            \"sovikhuan\": null,\r\n" + 
	            		"            \"cachsapxep\": null\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"CTK\",\r\n" + 
	            		"            \"gra\": \"(-)\",\r\n" + 
	            		"            \"grd\": null,\r\n" + 
	            		"            \"ten\": \"Cầu trực khuẩn\",\r\n" + 
	            		"            \"sovikhuan\": null,\r\n" + 
	            		"            \"cachsapxep\": null\r\n" + 
	            		"        },\r\n" + 
	            		"        {\r\n" + 
	            		"            \"ma\": \"KHAC\",\r\n" + 
	            		"            \"gra\": null,\r\n" + 
	            		"            \"grd\": null,\r\n" + 
	            		"            \"ten\": \"Khác\",\r\n" + 
	            		"            \"sovikhuan\": null,\r\n" + 
	            		"            \"cachsapxep\": null\r\n" + 
	            		"        }\r\n" + 
	            		"    ]\r\n" + 
	            		"}";
			Map<String,String> mapreqve = new HashMap<String, String>();
            
			mapreqve.put("payload", DatatypeConverter.printBase64Binary(datagocve.getBytes()));
			mapreqve.put("alg", "SHA1WithRSA");
			mapreqve.put("signature",signbase);
            
            get = client.Post("/api/v2/json/sign/verify",mapreqve);
            String ve = IOUtils.toString(get.getEntity().getContent(),StandardCharsets.UTF_8); 
            System.out.println("KQ ve:"+ve);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static PublicKey getpublickey(String cert) {
		try {
			byte[] certBytes = cert.getBytes(java.nio.charset.StandardCharsets.UTF_8);

			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			InputStream in = new ByteArrayInputStream(certBytes);
			X509Certificate certificate = (X509Certificate)certFactory.generateCertificate(in);
			return certificate.getPublicKey();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	public static void verify(String datagoc,String signature,PublicKey pub) {
		try {
			Signature sig = Signature.getInstance("SHA1WithRSA");

			sig.initVerify(pub);
			sig.update(datagoc.getBytes());
			Boolean reponse = sig.verify(DatatypeConverter.parseBase64Binary(signature));
			System.out.println (reponse);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
