package cybersignclient.hmac;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class ClientCallApi {

	ApiClient client = null;
	public ClientCallApi() throws KeyManagementException, MalformedURLException, KeyStoreException, NoSuchAlgorithmException, URISyntaxException {
		client = new ApiClient("https://api.cyberhsm.vn");
//		client.setCredentials("6f321d7961dd41d6b21d7961dd71d669", "MDU1YzU0ZjgwMzUyMTdkMjc5NDA5MzQxOTdiNWE3NDc4MmVlOWQwZTdiMjk3YjA1ZWM3ZDU3YjFmYTNmMjZlMw==");
//		client.setCredentials("2ca759fc5c4f4830a759fc5c4fd830cf", "YmQyMjlhNzZjY2FkNDEzNjI5Yjk2ZTg3MTFlZTEzZGI5MjYxZWUzMmZkMWUwNTczZDZlODMwYzlhOGFmYjgxYQ==");
		
		client.setCredentials("ff2dc8bb59914912adc8bb59918912a6", "N2M4YTQ2YWI5NzQ5NWZiNGVjZWNiMDdmNTJmODA1NWM1NTNkOTZjZWI2MzYxOGU2ZDYwOGFhNGFlMzEyMDJhNw==");
	}
	
	String getObject(String base64OfficeSigned) {
		
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(base64OfficeSigned).getAsJsonObject();
		String objStr = obj.get("base64officeSigned").getAsString();
		System.out.println("string: " + objStr);
		return objStr;
	}
	
	String getObject(String base64Signed, String object) {
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(base64Signed).getAsJsonObject();
		String objStr = obj.get(object).getAsString();
		System.out.println("string: " + objStr);
		return objStr;
	}
	
	
}
