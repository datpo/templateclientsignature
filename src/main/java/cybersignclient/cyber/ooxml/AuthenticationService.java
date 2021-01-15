package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.security.cert.X509Certificate;
import java.util.List;

public interface AuthenticationService {
 void validateCertificateChain(List<X509Certificate> var1) throws SecurityException;
}

