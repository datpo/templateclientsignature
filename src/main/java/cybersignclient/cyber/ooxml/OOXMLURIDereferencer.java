package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;

public class OOXMLURIDereferencer implements URIDereferencer {
 private final InputStream ooxmlIn;
 private final URIDereferencer baseUriDereferencer;

 public OOXMLURIDereferencer(InputStream ooxmlIn) {
     this.ooxmlIn = ooxmlIn;
     XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance();
     this.baseUriDereferencer = xmlSignatureFactory.getURIDereferencer();
 }

 public Data dereference(URIReference uriReference, XMLCryptoContext context) throws URIReferenceException {
     if (uriReference == null) {
         throw new NullPointerException("URIReference cannot be null");
     } else if (context == null) {
         throw new NullPointerException("XMLCrytoContext cannot be null");
     } else {
         String uri = uriReference.getURI();

         try {
             uri = URLDecoder.decode(uri, "UTF-8");
         } catch (UnsupportedEncodingException var7) {
             var7.printStackTrace();
         }

         try {
             InputStream dataInputStream = this.findDataInputStream(uri);
             this.ooxmlIn.reset();
             return (Data)(dataInputStream == null ? this.baseUriDereferencer.dereference(uriReference, context) : new OctetStreamData(dataInputStream, uri, (String)null));
         } catch (IOException var5) {
             throw new URIReferenceException("I/O error: " + var5.getMessage(), var5);
         } catch (InvalidFormatException var6) {
             throw new URIReferenceException("Invalid format error: " + var6.getMessage(), var6);
         }
     }
 }

 private InputStream findDataInputStream(String uri) throws IOException, InvalidFormatException {
     if (-1 != uri.indexOf("?")) {
         uri = uri.substring(0, uri.indexOf("?"));
     }

     OPCPackage pkg = OPCPackage.open(this.ooxmlIn);
     Iterator var4 = pkg.getParts().iterator();

     while(var4.hasNext()) {
         PackagePart part = (PackagePart)var4.next();
         if (uri.equals(part.getPartName().getURI().toString())) {
             return part.getInputStream();
         }
     }

     return null;
 }
}

