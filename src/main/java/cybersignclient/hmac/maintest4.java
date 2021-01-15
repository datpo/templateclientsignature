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

public class maintest4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 ApiClient client = null;
		try {
			/*List<apikey> l = kk();
			System.out.println(l.size());

			 int dem = 1;
			for(apikey item : l) {
				if(!item.apikey.trim().equals("")) {
			            //client = new ApiClient("http://api.cyberhsm.vn:8443");
					 client = new ApiClient("http://api.cyberhsm.vn:8443");
			            client.setCredentials(item.apikey, item.pass);

			            //B1 Gen otp
			            HttpResponse get = null;
			            ObjectMapper objectMapper = new ObjectMapper();
			            List<String> base64hash = new ArrayList<String>();
			            base64hash.add("ubqQXRVmcFaXNqKMEsTqcsOPN9o=");
			            base64hash.add("ubqQXRVmcFaXNqKMEsTqcsOPN9o=");
			            PdfListHashData reqbody = new PdfListHashData();
			            reqbody.setBase64hash(base64hash);
			            reqbody.setHashalg("SHA-1");
			            
			            ivanRequest ivan = new ivanRequest();
			            ivan.setBase64xml("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48SG9zbyB4bWxuczp4c2k9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIiB4bWxuczp4c2Q9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hIj48Tm9pRHVuZz48VGhvbmdUaW5JVkFOPjxNYUlWQU4+MDAxMDA8L01hSVZBTj48VGVuSVZBTj5Dw5RORyBUWSBD4buUIFBI4bqmTiBDWUJFUkxPVFVTPC9UZW5JVkFOPjwvVGhvbmdUaW5JVkFOPjxUaG9uZ1RpbkRvblZpPjxUZW5Eb2lUdW9uZz5Dw7RuZyB0eSBDeWJlcmxvdHVzPC9UZW5Eb2lUdW9uZz48TWFTb0JIWEg+VFRQMDAwOTwvTWFTb0JIWEg+PExvYWlEb2lUdW9uZz4xPC9Mb2FpRG9pVHVvbmc+PE1hU29UaHVlPjExMDIwMzA0NTU8L01hU29UaHVlPjxOZ3VvaUt5PktUVEtUVDwvTmd1b2lLeT48RGllblRob2FpPjAzODcyNTg2NjA8L0RpZW5UaG9haT48Q29RdWFuUXVhbkx5PjkwMzwvQ29RdWFuUXVhbkx5PjwvVGhvbmdUaW5Eb25WaT48VGhvbmdUaW5Ib1NvPjxUZW5UaHVUdWM+xJDEg25nIGvDvSwgxJFp4buBdSBjaOG7iW5oIMSRb8yBbmcgQkhYSCwgQkhZVCwgQkhUTiwgQkhUTkzEkCwgQk5OOyBD4bqlcCBz4buVIEJIWEgsIHRo4bq7IEJIWVQ8L1RlblRodVR1Yz48TWFUaHVUdWM+NjAwPC9NYVRodVR1Yz48S3lLZUtoYWk+MDgvMjAyMDwvS3lLZUtoYWk+PE5nYXlMYXA+MjIvMDgvMjAyMDwvTmdheUxhcD48U29MdW9uZ0ZpbGU+MTwvU29MdW9uZ0ZpbGU+PFF1eVRyaW5oSVNPIC8+PFRvS2hhaXM+PEZpbGVUb0toYWk+PE1hVG9LaGFpPkQwMi1UUzwvTWFUb0toYWk+PE1vVGFUb0toYWk+RGFuaCBzw6FjaCBsYW8gxJHhu5luZyB0aGFtIGdpYSBCSFhILCBCSFlULCBCSFROLCBCSFROTMSQLCBCTk48L01vVGFUb0toYWk+PFRlbkZpbGU+RDAyVFMueG1sPC9UZW5GaWxlPjxMb2FpRmlsZT4ueG1sPC9Mb2FpRmlsZT48RG9EYWlGaWxlPjQwMDc8L0RvRGFpRmlsZT48Tm9pRHVuZ0ZpbGU+UEQ5NGJXd2dkbVZ5YzJsdmJqMGlNUzR3SWlCbGJtTnZaR2x1WnowaVZWUkdMVGdpUHo0OFJEQXlMVlJUSUhodGJHNXpPbk5wWnowaWFIUjBjRG92TDNkM2R5NTNNeTV2Y21jdk1qQXdNQzh3T1M5NGJXeGtjMmxuSXlJZ2VHMXNibk02Y0hKdlBTSm9kSFJ3T2k4dlpYaGhiWEJzWlM1dmNtY3ZJM05wWjI1aGRIVnlaVkJ5YjNCbGNuUnBaWE1pSUhodGJHNXpPbmh6YVQwaWFIUjBjRG92TDNkM2R5NTNNeTV2Y21jdk1qQXdNUzlZVFV4VFkyaGxiV0V0YVc1emRHRnVZMlVpUGp4VWFHOXVaMVJwYmt0b1lXTStQRk52Vkc5Q2FXRlRiMEpJV0VnZ0x6NDhVMjlVYjFKdmFWTnZRa2hZU0NBdlBqeFRiMVJvWlVKSVdWUWdMejQ4TDFSb2IyNW5WR2x1UzJoaFl6NDhUbTlwUkhWdVp6NDhUR0Z2Ukc5dVp6NDhVMVJVUGpFOEwxTlVWRDQ4U0c5VVpXNCtRVUpEVG1kMWVlRzdoVzR5Tmp3dlNHOVVaVzQrUEUxaGMyOUNTRmhJUGprM01UWTFNVEF3TXprOEwwMWhjMjlDU0ZoSVBqeERhSFZqVm5VK1RtakRvbTRnZG1uRHFtNDhMME5vZFdOV2RUNDhUbTlwVEdGdFZtbGxZejVJVGp3dlRtOXBUR0Z0Vm1sbFl6NDhWR2xsYmt4MWIyNW5QalV3TURBd01EQXVNREE4TDFScFpXNU1kVzl1Wno0OFVHaDFRMkZ3UTFZK01DNHdNRHd2VUdoMVEyRndRMVkrUEZCb2RVTmhjRlJPVmtzK01DNHdNRHd2VUdoMVEyRndWRTVXU3o0OFVHaDFRMkZ3VkU1T1oyaGxQakF1TURBOEwxQm9kVU5oY0ZST1RtZG9aVDQ4VUdoMVEyRndUSFZ2Ym1jK01UQXdNREF3TUM0d01Ed3ZVR2gxUTJGd1RIVnZibWMrUEZCb2RVTmhjRUp2YzNWdVp6NHhNREF3TURBd0xqQXdQQzlRYUhWRFlYQkNiM04xYm1jK1BGUjFWR2hoYm1jK01EZ3ZNakF5TUR3dlZIVlVhR0Z1Wno0OFJHVnVWR2hoYm1jK01EZ3ZNakF5TUR3dlJHVnVWR2hoYm1jK1BFZG9hV05vZFQ1SXhKQk14SkFnYytHN2tTQlJ4SkF2UTNsaVpYSXdNU0J1WjhPZ2VTQXdNUzh3TlM4eU1ESXdQQzlIYUdsamFIVStQRkJCUGtGRVBDOVFRVDQ4Vkhsc1pVUnZibWMrTXpBdU1EQThMMVI1YkdWRWIyNW5QanhVYVc1b1RHRnBQakU4TDFScGJtaE1ZV2srUEVSaFEyOVRiejR3UEM5RVlVTnZVMjgrUEU1bllYbERhR1YwSUM4K1BFTnZSMmxoYlVOb1pYUStNRHd2UTI5SGFXRnRRMmhsZEQ0OFRYVmpTSFZ2Ym1jK016d3ZUWFZqU0hWdmJtYytQRTFoVUdodmJtZENZVzRnTHo0OFRXRjJkVzVuVTFNZ0x6NDhUV0YyZFc1blRGUlVQakF4UEM5TllYWjFibWRNVkZRK1BFbEVQakF3TURBd01EQTJNakk4TDBsRVBqeE1iMkZwUGpFOEwweHZZV2srUEM5TVlXOUViMjVuUGp3dlRtOXBSSFZ1Wno0OFEydDVQanhUYVdkdVlYUjFjbVVnU1dROUluTnBaMmxrSWlCNGJXeHVjejBpYUhSMGNEb3ZMM2QzZHk1M015NXZjbWN2TWpBd01DOHdPUzk0Yld4a2MybG5JeUkrUEZOcFoyNWxaRWx1Wm04K1BFTmhibTl1YVdOaGJHbDZZWFJwYjI1TlpYUm9iMlFnUVd4bmIzSnBkR2h0UFNKb2RIUndPaTh2ZDNkM0xuY3pMbTl5Wnk5VVVpOHlNREF4TDFKRlF5MTRiV3d0WXpFMGJpMHlNREF4TURNeE5TSWdMejQ4VTJsbmJtRjBkWEpsVFdWMGFHOWtJRUZzWjI5eWFYUm9iVDBpYUhSMGNEb3ZMM2QzZHk1M015NXZjbWN2TWpBd01DOHdPUzk0Yld4a2MybG5JM0p6WVMxemFHRXhJaUF2UGp4U1pXWmxjbVZ1WTJVZ1ZWSkpQU0lpUGp4VWNtRnVjMlp2Y20xelBqeFVjbUZ1YzJadmNtMGdRV3huYjNKcGRHaHRQU0pvZEhSd09pOHZkM2QzTG5jekxtOXlaeTh5TURBd0x6QTVMM2h0YkdSemFXY2paVzUyWld4dmNHVmtMWE5wWjI1aGRIVnlaU0lnTHo0OEwxUnlZVzV6Wm05eWJYTStQRVJwWjJWemRFMWxkR2h2WkNCQmJHZHZjbWwwYUcwOUltaDBkSEE2THk5M2QzY3Vkek11YjNKbkx6SXdNREF2TURrdmVHMXNaSE5wWnlOemFHRXhJaUF2UGp4RWFXZGxjM1JXWVd4MVpUNUVWMm9yUTFKbU1uUktTRGQxVHpKd1ZteFZRVUZwWWpBeVMyODlQQzlFYVdkbGMzUldZV3gxWlQ0OEwxSmxabVZ5Wlc1alpUNDhMMU5wWjI1bFpFbHVabTgrUEZOcFoyNWhkSFZ5WlZaaGJIVmxQbWcwTVN0TGRsQmtkbHBvTkhaVGFHZE1hRVJoVVVrMFVGcFJTMk5QWVZvMFUwTkpRM0EwVlZobk4wVjZVMlIwSzJweU9IUmFSWEl6WlUxTGVtbDZhR3RHVERsalowWnNOamhyYWxaeWVtbE1SRVpyYTBwc1lVVnJORXBsTjBONVV6bE9kV2hITW1zdll6WmxOVTVLYVZneWRuRTVjVEZZUWpSbEsweEpkbVpzTTNGbGNEbE5lREJ2TWtaQ2VtRTJka3BpZDJWTmRqVkdVRk40YURnclNrSmtiRGdyZGtKWVVXNXVPRDA4TDFOcFoyNWhkSFZ5WlZaaGJIVmxQanhMWlhsSmJtWnZQanhMWlhsV1lXeDFaVDQ4VWxOQlMyVjVWbUZzZFdVK1BFMXZaSFZzZFhNK2FVVlVVVmxNUlc1d2FUVTRTbWxoVlZVNE5uWlJaM2xrUnpCMU1VUnNSbEpyYVdscFRIUlhWME14Vld4S0syeElObnB4Y0ZVNFprTnJWM2MzVFRCelZ6WTBUSGxyVDFOT2FUSmhiV2xPVnpKWkwxcHFkMEpaYjA1R2IzRTVkVmxrVm1oNlJUbFBWVWR5T1dvMGNGaDVPWFJKZDNRck5tNTJlRGRGTjJWcGRqUjZNSFZuUXpGNmFVZElVbTFqZDJsdlRuRjBaSFJFV0RKcFUxUldNa2xOV2tsU1JIRlBaQzh6Y1M5elBUd3ZUVzlrZFd4MWN6NDhSWGh3YjI1bGJuUStRVkZCUWp3dlJYaHdiMjVsYm5RK1BDOVNVMEZMWlhsV1lXeDFaVDQ4TDB0bGVWWmhiSFZsUGp4WU5UQTVSR0YwWVQ0OFdEVXdPVk4xWW1wbFkzUk9ZVzFsUGs5SlJDNHdMamt1TWpNME1pNHhPVEl3TURNd01DNHhNREF1TVM0eFBVMVRWRG94TVRBeU1ETXdORFUxTENCRFRqMUR3NVJPUnlCVVdTQlQ0YnVzSUVUaHU2Uk9SeURFa09HN2dpQlVSVk5VTENCTVBVUGh1cWQxSUVkcDRicWxlU3dnVXoxSXc2QWdUdUc3bVdrc0lFTTlWazQ4TDFnMU1EbFRkV0pxWldOMFRtRnRaVDQ4V0RVd09VTmxjblJwWm1sallYUmxQazFKU1VWdVZFTkRRVFJYWjBGM1NVSkJaMGxSVmtGRlFrTlJkMlpaUkRaRVlYSm1ha3huWTBWeGFrRk9RbWRyY1docmFVYzVkekJDUVZGelJrRkVRbWROVVhOM1ExRlpSRlpSVVVkRmQwcFhWR3BGTmsxRVowZEJNVlZGUTJkM2VGRTRUMVZVYTJOblZrWnJaMUVyUnpkc1EwSlJVMDlITm5Cck5HZFdhMjVvZFRSU1QwbEdVa2wzTlZKUFVubENUMUpXWkZWU1ZYZDBWa1ZXVFZKVlRsQlVWRVZXVFVKTlIwRXhWVVZCZUUxTlZHdFdXRlpGVmsxTVZVNUNTVWhaZVUxQ05GaEVWRWwzVFVSUmVFNVVSWGhOYWxFeFRWWnZXRVJVU1hoTlZFVjVUMFJGZUUxcVVURk5WbTkzWjFsSmVFTjZRVXBDWjA1V1FrRlpWRUZzV2s5TlVrbDNSVUZaUkZaUlVVbEVRV3hKZHpaQloxUjFSemR0VjJ0NFJsUkJWRUpuVGxaQ1FXTk5SRVZRYUhWeFpERkpSV1J3TkdKeGJHVlVSVzlOUTFsSFFURlZSVUYzZDJaUk9FOVZWR3RqWjFaR2EyZFZLMGMzY2tOQ1JUUmlkV3RVYTJObmVFcEVhSFUwU1dkV1JWWlVWa1JGWlUxQ2QwZERaMjFUU205dFZEaHBlR3RCVVVWTlJHc3hWRlpFYjNoTlZFRjVUVVJOZDA1RVZURk5TVWRtVFVFd1IwTlRjVWRUU1dJelJGRkZRa0ZSVlVGQk5FZE9RVVJEUW1sUlMwSm5VVU5KVWs1Q1ozTlRaVzFNYm5kdFNuQlNWSHB4T1VORVNqQmlVemRWVDFWV1IxTkxTMGwxTVZwWlRGWlRWVzQyVldaeVQzRnNWSGc0UzFKaVJITjZVM2hpY21kMlMxRTFTVEpNV25GaFNURmlXbW81YlZCQlJtbG5NRmRwY2pJMWFERlhTRTFVTURWUllYWXlVR2xzWmt3eU1HcERNemR4WlM5SWMxUjBOa3N2YWxCVE5rRk1XRTlKV1dSSFducERTMmN5Y1RFeU1FNW1ZVXBLVGxoWlozaHJhRVZQYnpVekwyVnlLM2RKUkVGUlFVSnZORWxDYzJwRFEwRmhOSGRFUVZsRVZsSXdWRUZSU0M5Q1FVbDNRVVJCWmtKblRsWklVMDFGUjBSQlYyZENVME00VUVWb055ODFUMWRFVW5oaUwyMU5VV2M0TDBsa01WWnRla0p4UW1kbmNrSm5SVVpDVVdOQ1FWRlNaVTFHZDNkTVVWbEpTM2RaUWtKUlZVaE5RVXRIU1Zkb01HUklRVFpNZVRsM1pGZEpkV0p0VmpOWk1rVjFaRzAwZG1KdFZqTmtSMVp6VEZkT2FFeHRUbmxrUkVGeVFtZG5ja0puUlVaQ1VXTjNRVmxaWm1GSVVqQmpSRzkyVERJNWFtTXpRWGxNYlRWc1pESk9hRXh1V25WTU0wcHNZek5DZG1KdFVteGpha0ZZUW1kT1ZraFNSVVZGUkVGUFoxRjRhRmx0VGtGa1IxWjZaRU0xYW1JeU1IZFlaMWxFVmxJd1owSkdZM2RXVkVKVVFtZDNja0puUlVWQldVaDBRWGRGU2tGM1JYZFJla0ZxUW1kbmNrSm5SVVpDVVdORFFWSlpXR0ZJVWpCalJHOTJURE5DTVZscE5YVmFXR1JxV1ZNMU1tSnBPWGxqUjBWM1NFRlpTVXQzV1VKQ1VWVklRV2RKZDBWQmQwOVVNVTVtVkcxV00xaDZSazVZTVZKc1l6TlJkMDVCV1VSV1VqQnNRa013ZDB0M1dVbExkMWxDUWxGVlNFRjNTVWREUTNOSFFWRlZSa0ozVFVWQ1oyOXlRbWRGUlVGWlNUTkRaMDFOUW1kcmNXaHJhVWM1ZVRoQ1FWRlZkMDEzV1VSV1VqQm1Ra04zZDB0cVFXOXZRMkZuU2tsWmFXRklVakJqUkc5MlRESk9lV0pFU1hWaWJWWXpXVEpGZFdSdE5IWmliVll6WkVkV2MweFhUbWhNYlU1NVlrUkJaRUpuVGxaSVVUUkZSbWRSVlN0aE0yUXZValYzTUV0eWVISmxialJpZVZsaWRGUjRSM1ZCYzNkRVoxbEVWbEl3VUVGUlNDOUNRVkZFUVdkVWQwMUJNRWREVTNGSFUwbGlNMFJSUlVKRGQxVkJRVFJKUWtGUlFsRjVZbTlpY3pkM09HNUVhMXB6ZHpkWVJYSlVPR3A2VFVGNVVFSTJUa1ZzY1VReldrZ3ZiSFpOUXpRNE5HSndPV0VyYjNaUEszUnJMekIzTUN0WlIyMXlkWHBDVTNsQldrUlRiRFpwWkRKbGJuVjFSRXd6YkROU1VISTFSREpTV2l0WFJYWkpTbVpRSzI1V1FrTkxOWEpEYVRObFdHVk1XSGRxV1RjME5UQTVaVEJZU21aSlVuWXdWblpKV1VKR05qWTVkekJRTUdob2FFMWpiQzlzWmxZdlExSTFZMFFyVGtWdGFqRXlRMlo0WjNsbVlrcFpiVzl1ZG5oTlYxVjBkVE1yVXpSS05UWkdTRlZhUldOelZWaFVjRFppZW05UFUzVmhjakJvYW01VVVpOXlabU5KVVRGb04wcGFOelJuVGpKdUt6QklUMlpTT1dWTlVsZzFaVGxHYUVScWJuaE9Wbk5HVTBwa05HMXFNM1ZWV0RaMVMweFBiMHBMVUVwU2JqQXljR0YzVEVSS2NYZFZjR1poTUVGbmFuWnlhakJhVkhaWmRFMTFXbXRhWlM4dmRuQndjSEpJT0cwMlkzQllWblpsWWxKWFBDOVlOVEE1UTJWeWRHbG1hV05oZEdVK1BDOVlOVEE1UkdGMFlUNDhMMHRsZVVsdVptOCtQRTlpYW1WamRENDhVMmxuYm1GMGRYSmxVSEp2Y0dWeWRHbGxjeUJKWkQwaWNISnZhV1FpSUhodGJHNXpQU0lpUGp4VGFXZHVZWFIxY21WUWNtOXdaWEowZVNCVVlYSm5aWFE5SWlOemFXZHBaQ0krUEZOcFoyNXBibWRVYVcxbElIaHRiRzV6UFNKb2RIUndPaTh2WlhoaGJYQnNaUzV2Y21jdkkzTnBaMjVoZEhWeVpWQnliM0JsY25ScFpYTWlQakl3TWpBdE1EZ3RNakpVTURnNk16RTZOVFZhUEM5VGFXZHVhVzVuVkdsdFpUNDhMMU5wWjI1aGRIVnlaVkJ5YjNCbGNuUjVQand2VTJsbmJtRjBkWEpsVUhKdmNHVnlkR2xsY3o0OEwwOWlhbVZqZEQ0OEwxTnBaMjVoZEhWeVpUNDhMME5yZVQ0OEwwUXdNaTFVVXo0PTwvTm9pRHVuZ0ZpbGU+PC9GaWxlVG9LaGFpPjwvVG9LaGFpcz48L1Rob25nVGluSG9Tbz48L05vaUR1bmc+PENLeV9Edmk+PFNpZ25hdHVyZSBJZD0ic2lnaWQiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjIj48U2lnbmVkSW5mbz48Q2Fub25pY2FsaXphdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnL1RSLzIwMDEvUkVDLXhtbC1jMTRuLTIwMDEwMzE1IiAvPjxTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjcnNhLXNoYTEiIC8+PFJlZmVyZW5jZSBVUkk9IiI+PFRyYW5zZm9ybXM+PFRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNlbnZlbG9wZWQtc2lnbmF0dXJlIiAvPjwvVHJhbnNmb3Jtcz48RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI3NoYTEiIC8+PERpZ2VzdFZhbHVlPlhtSys1dVFTUU1lNkZzd1pKQUNmL3JWbzZ0ST08L0RpZ2VzdFZhbHVlPjwvUmVmZXJlbmNlPjwvU2lnbmVkSW5mbz48U2lnbmF0dXJlVmFsdWU+WDFPWnlhSWNibHJhOW44Yjl0OHoxZ2hVQU85VjRNQkRMWUhZWVd3SzE5RjJqUlZ5eFVhZ0Z0dzVXQjVFdFRlVm9rdDIwMWxxaUdaSSs3T2RRZXc3emI5Um43aktCaGxyTjFPSjZiV0hhaTcxOEl4WjVwdUU1NlRrNEpPZXFGOFBNdE1Ta0x5VTlWWFRsdEUyUkQ4czZLUjBxNU5WbEJsSisrZlIvR1ZnaXVFPTwvU2lnbmF0dXJlVmFsdWU+PEtleUluZm8+PEtleVZhbHVlPjxSU0FLZXlWYWx1ZT48TW9kdWx1cz5pRVRRWUxFbnBpNThKaWFVVTg2dlFneWRHMHUxRGxGUmtpaWlMdFdXQzFVbEorbEg2enFwVThmQ2tXdzdNMHNXNjRMeWtPU05pMmFtaU5XMlkvWmp3QllvTkZvcTl1WWRWaHpFOU9VR3I5ajRwWHk5dEl3dCs2bnZ4N0U3ZWl2NHowdWdDMXppR0hSbWN3aW9OcXRkdERYMmlTVFYySU1aSVJEcU9kLzNxL3M9PC9Nb2R1bHVzPjxFeHBvbmVudD5BUUFCPC9FeHBvbmVudD48L1JTQUtleVZhbHVlPjwvS2V5VmFsdWU+PFg1MDlEYXRhPjxYNTA5U3ViamVjdE5hbWU+T0lELjAuOS4yMzQyLjE5MjAwMzAwLjEwMC4xLjE9TVNUOjExMDIwMzA0NTUsIENOPUPDlE5HIFRZIFPhu6wgROG7pE5HIMSQ4buCIFRFU1QsIEw9Q+G6p3UgR2nhuqV5LCBTPUjDoCBO4buZaSwgQz1WTjwvWDUwOVN1YmplY3ROYW1lPjxYNTA5Q2VydGlmaWNhdGU+TUlJRW5UQ0NBNFdnQXdJQkFnSVFWQUVCQ1F3ZllENkRhcmZqTGdjRXFqQU5CZ2txaGtpRzl3MEJBUXNGQURCZ01Rc3dDUVlEVlFRR0V3SldUakU2TURnR0ExVUVDZ3d4UThPVVRrY2dWRmtnUStHN2xDQlFTT0c2cGs0Z1Zrbmh1NFJPSUZSSXc1Uk9SeUJPUlZkVVJVd3RWRVZNUlVOUFRURVZNQk1HQTFVRUF4TU1Ua1ZYVkVWTUxVTkJJSFl5TUI0WERUSXdNRFF4TlRFeE1qUTFNVm9YRFRJeE1URXlPREV4TWpRMU1Wb3dnWUl4Q3pBSkJnTlZCQVlUQWxaT01SSXdFQVlEVlFRSURBbEl3NkFnVHVHN21Xa3hGVEFUQmdOVkJBY01ERVBodXFkMUlFZHA0YnFsZVRFb01DWUdBMVVFQXd3ZlE4T1VUa2NnVkZrZ1UrRzdyQ0JFNGJ1a1RrY2d4SkRodTRJZ1ZFVlRWREVlTUJ3R0NnbVNKb21UOGl4a0FRRU1EazFUVkRveE1UQXlNRE13TkRVMU1JR2ZNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0R05BRENCaVFLQmdRQ0lSTkJnc1NlbUxud21KcFJUenE5Q0RKMGJTN1VPVVZHU0tLSXUxWllMVlNVbjZVZnJPcWxUeDhLUmJEc3pTeGJyZ3ZLUTVJMkxacWFJMWJaajltUEFGaWcwV2lyMjVoMVdITVQwNVFhdjJQaWxmTDIwakMzN3FlL0hzVHQ2Sy9qUFM2QUxYT0lZZEdaekNLZzJxMTIwTmZhSkpOWFlneGtoRU9vNTMvZXIrd0lEQVFBQm80SUJzakNDQWE0d0RBWURWUjBUQVFIL0JBSXdBREFmQmdOVkhTTUVHREFXZ0JTQzhQRWg3LzVPV0RSeGIvbU1RZzgvSWQxVm16QnFCZ2dyQmdFRkJRY0JBUVJlTUZ3d0xRWUlLd1lCQlFVSE1BS0dJV2gwZEhBNkx5OXdkV0l1Ym1WM1kyRXVkbTR2Ym1WM2RHVnNMV05oTG1OeWREQXJCZ2dyQmdFRkJRY3dBWVlmYUhSMGNEb3ZMMjlqYzNBeUxtNWxkMk5oTG5adUwzSmxjM0J2Ym1SbGNqQVhCZ05WSFJFRUVEQU9nUXhoWW1OQWRHVnpkQzVqYjIwd1hnWURWUjBnQkZjd1ZUQlRCZ3dyQmdFRUFZSHRBd0VKQXdFd1F6QWpCZ2dyQmdFRkJRY0NBUllYYUhSMGNEb3ZMM0IxWWk1dVpYZGpZUzUyYmk5eWNHRXdIQVlJS3dZQkJRVUhBZ0l3RUF3T1QxTmZUbVYzWHpGTlgxUmxjM1F3TkFZRFZSMGxCQzB3S3dZSUt3WUJCUVVIQXdJR0NDc0dBUVVGQndNRUJnb3JCZ0VFQVlJM0NnTU1CZ2txaGtpRzl5OEJBUVV3TXdZRFZSMGZCQ3d3S2pBb29DYWdKSVlpYUhSMGNEb3ZMMk55YkRJdWJtVjNZMkV1ZG00dmJtVjNkR1ZzTFdOaExtTnliREFkQmdOVkhRNEVGZ1FVK2EzZC9SNXcwS3J4cmVuNGJ5WWJ0VHhHdUFzd0RnWURWUjBQQVFIL0JBUURBZ1R3TUEwR0NTcUdTSWIzRFFFQkN3VUFBNElCQVFCUXlib2JzN3c4bkRrWnN3N1hFclQ4anpNQXlQQjZORWxxRDNaSC9sdk1DNDg0YnA5YStvdk8rdGsvMHcwK1lHbXJ1ekJTeUFaRFNsNmlkMmVudXVETDNsM1JQcjVEMlJaK1dFdklKZlArblZCQ0s1ckNpM2VYZUxYd2pZNzQ1MDllMFhKZklSdjBWdklZQkY2Njl3MFAwaGhoTWNsL2xmVi9DUjVjRCtORW1qMTJDZnhneWZiSlltb252eE1XVXR1MytTNEo1NkZIVVpFY3NVWFRwNmJ6b09TdWFyMGhqblRSL3JmY0lRMWg3Slo3NGdOMm4rMEhPZlI5ZU1SWDVlOUZoRGpueE5Wc0ZTSmQ0bWozdVVYNnVLTE9vSktQSlJuMDJwYXdMREpxd1VwZmEwQWdqdnJqMFpUdll0TXVaa1plLy92cHBwckg4bTZjcFhWdmViUlc8L1g1MDlDZXJ0aWZpY2F0ZT48L1g1MDlEYXRhPjwvS2V5SW5mbz48T2JqZWN0PjxTaWduYXR1cmVQcm9wZXJ0aWVzIElkPSJwcm9pZCIgeG1sbnM9IiI+PFNpZ25hdHVyZVByb3BlcnR5IFRhcmdldD0iI3NpZ2lkIj48U2lnbmluZ1RpbWUgeG1sbnM9Imh0dHA6Ly9leGFtcGxlLm9yZy8jc2lnbmF0dXJlUHJvcGVydGllcyI+MjAyMC0wOC0yMlQwODozMTo1NVo8L1NpZ25pbmdUaW1lPjwvU2lnbmF0dXJlUHJvcGVydHk+PC9TaWduYXR1cmVQcm9wZXJ0aWVzPjwvT2JqZWN0PjwvU2lnbmF0dXJlPjwvQ0t5X0R2aT48L0hvc28+");
			            ivan.setHashalg("SHA1");
			            ivan.setXpathdata("/Hoso/NoiDung");
			            ivan.setXpathsign("/Hoso/CKy_iVan");
			            
			            XmlInvoiceData inv = new XmlInvoiceData();
			            inv.setHashalg("SHA1");
			            inv.setBase64xml("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48RGF0YT48SERvbkRUdT48VFRpbkhEb24+PE1hdUhEb24+MDFHVEtUMC8zNDU8L01hdUhEb24+PExvYWlIRG9uPjAxR1RLVDwvTG9haUhEb24+PFRlbkhEb24+SPNhID8/biBnaeEgdHI/IGdpYSB0P25nPC9UZW5IRG9uPjxUVGU+PE1hVFRlPlZORDwvTWFUVGU+PFRHaWE+MTwvVEdpYT48L1RUZT48SFRodWNIRG9uR29jPjAxPC9IVGh1Y0hEb25Hb2M+PE5nYXlIRG9uPjIwMTktMDYtMTFUMDI6MTM6MjUuMDAwKzA3OjAwPC9OZ2F5SERvbj48UEJhbj4xLjA8L1BCYW4+PFNvSERvbj4wMDAwMDE4PC9Tb0hEb24+PFRpbmhDaGF0SEQ+MDE8L1RpbmhDaGF0SEQ+PEtIaWV1SERvbj5BQS8xOEU8L0tIaWV1SERvbj48TVNUPjAxMDEzNjA2OTc8L01TVD48L1RUaW5IRG9uPjxORHVuZ0hEb24+PE5CYW4+PE5IYW5nLz48VEtob2FuLz48RFRob2FpPjA5ODkzMTc4OTA8L0RUaG9haT48RmF4Lz48Tmd1b2lMYXBIRG9uPjAxMDEzNjA2OTc8L05ndW9pTGFwSERvbj48TmdheUxhcEhEb24+MjAxOS0wNi0xMVQwMjoxMzoyNS4wMDArMDc6MDA8L05nYXlMYXBIRG9uPjxOZ3VvaVBEdXlldEhEb24vPjxUZW5EVmk+Q9RORyBUWSBUTkhIIE0/VCBUSMBOSCBWScpOID9ORyBEP05HIEs/IFRIVT9UIFbAPC9UZW5EVmk+PERDaGkvPjxFbWFpbD5jaGluaHZkQHRlY2Fwcm8uY29tLnZuPC9FbWFpbD48TVNUPjAxMDEzNjA2OTc8L01TVD48L05CYW4+PFRUb2FuPjxUb25nVGllblRydW9jVGh1ZTBWTkQ+MDwvVG9uZ1RpZW5UcnVvY1RodWUwVk5EPjxUb25nVGllblRodWUwVk5EPjA8L1RvbmdUaWVuVGh1ZTBWTkQ+PFRvbmdUaWVuVFRvYW5CQ2h1Lz48VG9uZ1RpZW5TYXVUaHVlMTBOVGU+MDwvVG9uZ1RpZW5TYXVUaHVlMTBOVGU+PFRvbmdUaWVuVHJ1b2NUaHVlS0NoaXVUaHVlTlRlPjA8L1RvbmdUaWVuVHJ1b2NUaHVlS0NoaXVUaHVlTlRlPjxUb25nVGllblNhdVRodWUwVk5EPjA8L1RvbmdUaWVuU2F1VGh1ZTBWTkQ+PFRvbmdUaWVuU2F1VGh1ZUtDaGl1VGh1ZVZORD4wPC9Ub25nVGllblNhdVRodWVLQ2hpdVRodWVWTkQ+PFRvbmdUaWVuVHJ1b2NUaHVlS0NoaXVUaHVlVk5EPjA8L1RvbmdUaWVuVHJ1b2NUaHVlS0NoaXVUaHVlVk5EPjxUb25nVGllblRodWU1TlRlPjA8L1RvbmdUaWVuVGh1ZTVOVGU+PFRvbmdUaWVuU2F1VGh1ZUtDaGl1VGh1ZU5UZT4wPC9Ub25nVGllblNhdVRodWVLQ2hpdVRodWVOVGU+PFRvbmdUaWVuU2F1VGh1ZTVOVGU+MDwvVG9uZ1RpZW5TYXVUaHVlNU5UZT48VG9uZ1RpZW5OVGU+MDwvVG9uZ1RpZW5OVGU+PFRvbmdUaWVuVHJ1b2NUaHVlMTBWTkQ+MDwvVG9uZ1RpZW5UcnVvY1RodWUxMFZORD48VG9uZ1RpZW5UaHVlVk5EPjA8L1RvbmdUaWVuVGh1ZVZORD48VG9uZ1RpZW5UaHVlMTBWTkQ+MDwvVG9uZ1RpZW5UaHVlMTBWTkQ+PFRvbmdUaWVuVHJ1b2NUaHVlNU5UZT4wPC9Ub25nVGllblRydW9jVGh1ZTVOVGU+PFRvbmdUaWVuS0NoaXVUaHVlVk5EPjA8L1RvbmdUaWVuS0NoaXVUaHVlVk5EPjxUb25nVGllblRodWUxME5UZT4wPC9Ub25nVGllblRodWUxME5UZT48VG9uZ1RpZW5UaHVlTlRlPjA8L1RvbmdUaWVuVGh1ZU5UZT48SFRodWNUVG9hbj5UTTwvSFRodWNUVG9hbj48VG9uZ1RpZW5UcnVvY1RodWUxME5UZT4wPC9Ub25nVGllblRydW9jVGh1ZTEwTlRlPjxUb25nVGllbkNLaGF1Vk5EPjA8L1RvbmdUaWVuQ0toYXVWTkQ+PFRvbmdUaWVuU2F1VGh1ZTVWTkQ+MDwvVG9uZ1RpZW5TYXVUaHVlNVZORD48VG9uZ1RpZW5UcnVvY1RodWU1Vk5EPjA8L1RvbmdUaWVuVHJ1b2NUaHVlNVZORD48VG9uZ1RpZW5WTkQ+MDwvVG9uZ1RpZW5WTkQ+PFRvbmdUaWVuVFRvYW5OVGU+MDwvVG9uZ1RpZW5UVG9hbk5UZT48VG9uZ1RpZW5TYXVUaHVlME5UZT4wPC9Ub25nVGllblNhdVRodWUwTlRlPjxUb25nVGllbktDaGl1VGh1ZU5UZT4wPC9Ub25nVGllbktDaGl1VGh1ZU5UZT48VG9uZ1RpZW5UcnVvY1RodWUwTlRlPjA8L1RvbmdUaWVuVHJ1b2NUaHVlME5UZT48VG9uZ1RpZW5UaHVlME5UZT4wPC9Ub25nVGllblRodWUwTlRlPjxUb25nVGllblNhdVRodWUxMFZORD4wPC9Ub25nVGllblNhdVRodWUxMFZORD48VG9uZ1RpZW5UVG9hblZORD4wPC9Ub25nVGllblRUb2FuVk5EPjxUb25nVGllbkNLaGF1TlRlPjA8L1RvbmdUaWVuQ0toYXVOVGU+PFRvbmdUaWVuVGh1ZTVWTkQ+MDwvVG9uZ1RpZW5UaHVlNVZORD48L1RUb2FuPjxOTXVhPjxOSGFuZy8+PE1hTk11YS8+PFRLaG9hbi8+PERUaG9haS8+PEhvVGVuPnZkYzwvSG9UZW4+PEZheC8+PFRlbkRWaT52ZGM8L1RlbkRWaT48RENoaS8+PEVtYWlsLz48TXN0Lz48L05NdWE+PERTYWNoSEhvYURWdS8+PC9ORHVuZ0hEb24+PC9IRG9uRFR1PjxUVGluS2hhYz48TWFRUj5YTjgyeFh3YUh0dDl4RmFmYlIyM1FqZGtlOXI0SlkzSHNBVTUzQVFRSFc2YVBkOUpMWWJyNGlBQzVjTDhSVnZSOzAxMDEzNjA2OTc7MDAwMDAxODsyMDE5NTExMjEzMjU8L01hUVI+PC9UVGluS2hhYz48L0RhdGE+");
			            
			            PdfHashData pdfh = new PdfHashData();
			            
			            //pdfh.setBase64hash("GNxzalLPMu32v3udkfISrpk5ZO4=");
			            pdfh.setBase64hash("OTP1kqjU7gaKNLwjjHisTmXRpmI=");
			            pdfh.setHashalg("SHA1");
			            get = client.Post("/api/pdf/sign/hashdata",pdfh);
			            
			             String theString = IOUtils.toString(get.getEntity().getContent(),StandardCharsets.UTF_8); 
			             restest kj = new Gson().fromJson(theString, restest.class);
			            
			             if(kj.status == 0) {
			            	 System.out.println(theString);
			            	 System.out.println("Thanh cong:"+item.apikey);
			             }else {
			            	 System.out.println(item.cmnd.replace("E8", "").replace(".", ""));
			             }
			            
				}
			}
			System.out.println(dem);
			if(true) {
				return;
			}*/
			//7725e7fbb8e74958a5e7fbb8e71958a3
//			 client = new ApiClient("https://api.cyberhsm.vn/");
			/*RSAPublicKey pub =  getPublicKey("C:\\Users\\thaidt\\Downloads\\01234567891.b64.key");
			
	            String signgg = "O7HMR8ovlLxa4po22Rj7x/FUCtAThDqBrcAuIrFWXyoOr8jPNtdnGI8kD/jVZzPTG1Dti5JeX85CnUnp+CzHBFc/qiJYnlMAhOANRFe/yoRR/WSNglyRiWINYAWfhUAEp0V9YUHujHLsJ1SR30QT4LLDkw5YMhdaJXMcGkngmTNxoDIBEtfjN0y5mxiMTRmwlXi+q753ZgQlazTirxZjwdDLZYnw0Hz46u9IvY6CKOkK1NpS3+W64XsOUCs+V8+yC1ydhw5KPh6PIvQC48feDXTQ9Feu4axUPuhs/IDxno/SvBlN+qTQJQy0U7qKK+x3qwa8KRLEFcGdWDvRkgzWyw==";
	             Signature sig = Signature.getInstance("SHA1WithRSA");

	             sig.initVerify(pub);
	             sig.update("123".getBytes());
	             Boolean re = sig.verify(DatatypeConverter.parseBase64Binary(signgg));
	             System.out.println (re);
	             if(true) {
		            	return;
		            }*/
			for(int i = 0 ; i < 100; i ++) {
			client = new ApiClient("http://localhost:8089");
	          //  client = new ApiClient("http://api.cyberhsm.vn:8443");
	            //client = new ApiClient("http://192.168.100.111");
	            //client.setCredentials("131783de2a2445539783de2a241553aa", "NDI5ZWE1MDY1NGNmMWY2M2IzNDBmOGQ4NjI0NmQzZTM3ZWFkNTAwYmE5MDYxZDY3NzU1MTYxN2M0NGY5NjJhZg==");
	         //stid   client.setCredentials("a6020d05702b46da820d05702be6daca", "NzA1ZWQwOGZlNmUzZWIwYjAxNzFhOGFjZGIxOTNjNGFhNzkzNGUzNDA5MDgxY2MyN2NjNmI5ZjI0MDlmNGYzMQ==");
			client.setCredentials("47dd2c92dfa246899d2c92dfa2868986", "MjhmYzdhZDkxZjI4ZWI2Y2FmNWIxYmEyMWRjNmMxMzM4NjI1Y2JmZDEzNjE2MWRhNWFkZmZkNjViNjAxYTk4Zg==");
			 //client.setCredentials("b66a3a66b8c94d4daa3a66b8c9dd4d58", "OTNkMzk4MGNlM2FjNGRlZDMyZWU1NDBmOTViMTZhZWQ3NWUxMjE3ODZiOGE1MmUwZGJjZGMwYzIxNTgzNjQyZA==");

	            //B1 Gen otp
	            HttpResponse get = null;
	           
	            Map<String,String> mapreq = new HashMap<String, String>();
	            String datagoc = "123";
	            mapreq.put("payload", DatatypeConverter.printBase64Binary(datagoc.getBytes()));
	            mapreq.put("alg", "SHA1withRSA");
	            get = client.Post("/api/bin/sign/base64",mapreq);
	           // get = client.Get("/api/account/endcert");
	            
	            //System.out.println(EntityUtils.toString(get.getEntity()));
	            //get.getEntity().getContent();
	           // ApiResp resp = objectMapper.readValue(get.getEntity().getContent(), ApiResp.class);
	            //String resp = objectMapper.readValue(get.getEntity().getContent(), String.class);
	             String theString = IOUtils.toString(get.getEntity().getContent(),StandardCharsets.UTF_8); 
	             System.out.println(theString);
	             System.out.println("Gia công");
	           
//	           for(ApiResp i : resp) {
//	        	   System.out.println(i.getObj());
//	           }
			}
	            
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
