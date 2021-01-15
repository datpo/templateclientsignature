package cybersignclient.hmac;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClientCallApi {

	ApiClient client = null;
	public ClientCallApi() throws KeyManagementException, MalformedURLException, KeyStoreException, NoSuchAlgorithmException, URISyntaxException {
		client = new ApiClient("https://api.cyberhsm.vn");
		client.setCredentials("6f321d7961dd41d6b21d7961dd71d669", "MDU1YzU0ZjgwMzUyMTdkMjc5NDA5MzQxOTdiNWE3NDc4MmVlOWQwZTdiMjk3YjA1ZWM3ZDU3YjFmYTNmMjZlMw==");
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
