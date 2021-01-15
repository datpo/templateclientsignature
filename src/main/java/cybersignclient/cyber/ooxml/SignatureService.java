package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface SignatureService {
 String getFilesDigestAlgorithm();

 DigestInfo preSign(List<DigestInfo> var1, List<X509Certificate> var2) throws NoSuchAlgorithmException;

 void postSign(byte[] var1, List<X509Certificate> var2);
}
