package cybersignclient.hmac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class maintest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 ApiClient client = null;
		try {
			client = new ApiClient("http://localhost:8089");
			client.setCredentials("47dd2c92dfa246899d2c92dfa2868986", "MjhmYzdhZDkxZjI4ZWI2Y2FmNWIxYmEyMWRjNmMxMzM4NjI1Y2JmZDEzNjE2MWRhNWFkZmZkNjViNjAxYTk4Zg==");
	            HttpResponse get = null;
	            Map<String,String> mapreq = new HashMap<String, String>();
	            String datagoc = "123";
	            mapreq.put("payload", DatatypeConverter.printBase64Binary(datagoc.getBytes()));
	            mapreq.put("alg", "SHA1withRSA");
	           // get = client.Post("/api/bin/sign/base64",mapreq);
	            get = client.Get("/api/account/endcert");
	             String theString = IOUtils.toString(get.getEntity().getContent(),StandardCharsets.UTF_8); 
	             System.out.println(theString);
	           
//	           for(ApiResp i : resp) {
//	        	   System.out.println(i.getObj());
//	           }
	            
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			}
	}
	private static String getKey(String filename) throws IOException {
	    // Read key from file
	    String strKeyPEM = "";
	    BufferedReader br = new BufferedReader(new FileReader(filename));
	    String line;
	    while ((line = br.readLine()) != null) {
	        strKeyPEM += line + "\n";
	    }
	    br.close();
	    return strKeyPEM;
	}
	public static RSAPublicKey getPublicKey(String filename) throws IOException, GeneralSecurityException {
	    String publicKeyPEM = getKey(filename);
	    return getPublicKeyFromString(publicKeyPEM);
	}

	public static RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
	    String publicKeyPEM = key;
	    publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
	    publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
	    byte[] encoded = DatatypeConverter.parseBase64Binary(publicKeyPEM);
	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
	    return pubKey;
	}
	public static PublicKey getPublicKey1(String filename)
		    throws Exception {

		    byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

		    X509EncodedKeySpec spec =
		      new X509EncodedKeySpec(keyBytes);
		    KeyFactory kf = KeyFactory.getInstance("RSA");
		    return kf.generatePublic(spec);
		  }
	private static final String FILE_NAME = "C:\\Users\\thaidt\\Desktop\\nhisan.xlsx";
	public static List<apikey> kk() {
		try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            List<apikey> listres = new ArrayList<apikey>();
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                	int index = 0;
                	apikey iapik = new apikey();
                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();
                    //getCellTypeEnum shown as deprecated for version 3.15
                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                    if(index == 0) {
                    	if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    		if(currentCell.getStringCellValue().toString().trim().equals("")) {
                    			continue;
                    		}else {
                    			iapik.apikey = currentCell.getStringCellValue();
                    		}
                    		
                        } 
                    }else if(index == 1) {
                    	if (currentCell.getCellTypeEnum() == CellType.STRING) {
                    		iapik.pass = currentCell.getStringCellValue();
                        } 
                    }
                    else if(index == 2) {
                    	if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                    		iapik.cmnd = currentCell.getNumericCellValue()+"";
                        } 
                    }
                    index++;
                    

                }
              //  System.out.println();
                listres.add(iapik);
            }
            return listres;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}
}
