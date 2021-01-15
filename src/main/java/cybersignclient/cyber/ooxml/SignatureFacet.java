package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface SignatureFacet {
 void preSign(XMLSignatureFactory var1, Document var2, String var3, List<X509Certificate> var4, List<Reference> var5, List<XMLObject> var6) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;

 void postSign(Element var1, List<X509Certificate> var2);
}
