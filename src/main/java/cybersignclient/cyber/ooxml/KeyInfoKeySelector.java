package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.KeySelector.Purpose;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

public class KeyInfoKeySelector extends KeySelector implements KeySelectorResult {
 private X509Certificate certificate;
 private X509Certificate[] certChain;

 public KeyInfoKeySelector() {
 }

 public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {
     ArrayList certList = new ArrayList();
     if (keyInfo == null) {
         throw new KeySelectorException("no ds:KeyInfo present");
     } else {
         List keyInfoContent = keyInfo.getContent();
         this.certificate = null;
         Iterator i$ = keyInfoContent.iterator();

         do {
             XMLStructure keyInfoStructure;
             do {
                 if (!i$.hasNext()) {
                     throw new KeySelectorException("No key found!");
                 }

                 keyInfoStructure = (XMLStructure)i$.next();
             } while(!(keyInfoStructure instanceof X509Data));

             X509Data x509Data = (X509Data)keyInfoStructure;
             List x509DataList = x509Data.getContent();
             Iterator i1$ = x509DataList.iterator();

             while(i1$.hasNext()) {
                 Object x509DataObject = i1$.next();
                 if (x509DataObject instanceof X509Certificate) {
                     certList.add(x509DataObject);
                 }
             }
         } while(certList.isEmpty());

         this.certChain = (X509Certificate[])certList.toArray(new X509Certificate[0]);
         this.certificate = this.certChain[0];
         return this;
     }
 }

 public Key getKey() {
     return this.certificate.getPublicKey();
 }

 public X509Certificate getCertificate() {
     return this.certificate;
 }

 public X509Certificate[] getCertChain() {
     return this.certChain;
 }
}
