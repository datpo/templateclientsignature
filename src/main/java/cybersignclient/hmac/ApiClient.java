package cybersignclient.hmac;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

//@Slf4j
public class ApiClient {

    private static final String APPLICATION_JSON = "application/json";
    private final String scheme;
    private final String host;
    private int port;
    private String apiKey;
    private byte[] apiSecret;
    private URI uri;

    private SSLContext sslContext;

    public ApiClient(String baseUri) throws MalformedURLException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        uri = new URI(baseUri);
        this.scheme = uri.getScheme();
        this.host = uri.getHost();
        this.port = uri.getPort();
        if(port <= 0)
        {
            if(scheme.compareToIgnoreCase("https")==0)
            {
                this.port = 443;
            }
            else if(scheme.compareToIgnoreCase("http")==0)
            {
                this.port = 80;
            }
        }
        sslContext = new SSLContextBuilder() .loadTrustMaterial(null, (x509CertChain, authType) -> true) .build();
    }

    public HttpResponse Get(String resource) throws IOException {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        String dateString = dateFormat.format(date);

        String nonce = UUID.randomUUID().toString();
        final HmacSignatureBuilder signatureBuilder = new HmacSignatureBuilder()
                .algorithm("HmacSHA256")
                .scheme(scheme)
                .host(host+":" + port)
                .method("GET")
                .resource(resource)
                .apiKey(apiKey)
                .nonce(nonce)
                .date(dateString)
                .apiSecret(apiSecret)
                .payload("".getBytes());
        final String signature = signatureBuilder.buildAsBase64String();
        final String authHeader = signatureBuilder.getAlgorithm() + " " + apiKey + ":" + nonce + ":" + signature + ":" + date.getTime()/1000 ;
        HttpClient client = HttpClientBuilder.create().setSSLContext(sslContext).setConnectionManager(new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)).build())).build();
        HttpUriRequest req = RequestBuilder.get().setUri(uri.toString()+resource)
                //.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .setHeader(HttpHeaders.DATE, dateString)
                .setHeader(HttpHeaders.AUTHORIZATION, authHeader)
                .build();
        System.out.println(uri.toString()+resource);
        System.out.println(dateString);
        System.out.println(authHeader);
        return client.execute(req);

    }

    public HttpResponse Post(String resource) throws IOException {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        String dateString = dateFormat.format(date);
        String nonce = UUID.randomUUID().toString();
        String content_type = APPLICATION_JSON;

        ObjectMapper objectMapper = new ObjectMapper();


        final HmacSignatureBuilder signatureBuilder = new HmacSignatureBuilder()
            .algorithm("HmacSHA256")
            .scheme(scheme)
            .host(host+":" + port)
            .method("POST")
            .resource(resource)
            .contentType(content_type)
            .apiKey(apiKey)
            .nonce(nonce)
            .date(dateString)
            .payload("".getBytes())
            .apiSecret(apiSecret);
        final String signature = signatureBuilder.buildAsBase64String();
        final String authHeader = signatureBuilder.getAlgorithm() + " " + apiKey + ":" + nonce + ":" + signature + ":" + date.getTime()/1000 ;
        HttpClient client = HttpClientBuilder.create().setSSLContext(sslContext).setConnectionManager(new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)).build())).build();
        HttpUriRequest req = RequestBuilder.post(uri.toString()+resource)
            .setHeader(HttpHeaders.CONTENT_TYPE, content_type)
            .setHeader(HttpHeaders.DATE, dateString)
            .setHeader(HttpHeaders.AUTHORIZATION, authHeader)
            .build();
        return client.execute(req);
    }

    public HttpResponse Post(String resource, Object data) throws IOException {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        String dateString = dateFormat.format(date);
        String nonce = UUID.randomUUID().toString();
        String content_type = APPLICATION_JSON;

        ObjectMapper objectMapper = new ObjectMapper();
        String svalue;
        if(data instanceof  String)
        {
            svalue = (String)data;
        }
        else
        {
            svalue = objectMapper.writeValueAsString(data);
        }
        byte[] bytes = svalue.getBytes(Charset.forName("UTF-8"));
        StringEntity stringEntity = new StringEntity(svalue,Charset.forName("UTF-8"));
        final HmacSignatureBuilder signatureBuilder = new HmacSignatureBuilder()
                .algorithm("HmacSHA256")
                .scheme(scheme)
                .host(host+":" + port)
                .method("POST")
                .resource(resource)
                .contentType(content_type)
                .apiKey(apiKey)
                .nonce(nonce)
                .date(dateString)
                .apiSecret(apiSecret)
                .payload(bytes);
       // log.info("bytes length: "+ bytes.length);
        final String signature = signatureBuilder.buildAsBase64String();
        final String authHeader = signatureBuilder.getAlgorithm() + " " + apiKey + ":" + nonce + ":" + signature + ":" + date.getTime()/1000 ;
        HttpClient client = HttpClientBuilder.create().setSSLContext(sslContext).setConnectionManager(new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)).build())).build();
        HttpUriRequest req = RequestBuilder.post(uri.toString()+resource)
                .setHeader(HttpHeaders.CONTENT_TYPE, content_type)
                .setHeader(HttpHeaders.DATE, dateString)
                .setHeader(HttpHeaders.AUTHORIZATION, authHeader)
                .setEntity(stringEntity)
                .build();
        return client.execute(req);
    }
    public void setCredentials(String apiID, String secret) {
        this.apiKey = apiID;
        this.apiSecret = Base64.getDecoder().decode(secret);

    }
}
