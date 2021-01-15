package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import org.w3c.dom.Document;

public interface SignatureAspect {
 void preSign(XMLSignatureFactory var1, Document var2, String var3, List<Reference> var4, List<XMLObject> var5) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException;
}
