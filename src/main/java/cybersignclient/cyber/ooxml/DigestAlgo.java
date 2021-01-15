package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//


public enum DigestAlgo {
 SHA1("SHA-1", "http://www.w3.org/2000/09/xmldsig#sha1"),
 SHA256("SHA-256", "http://www.w3.org/2001/04/xmlenc#sha256"),
 SHA512("SHA-512", "http://www.w3.org/2001/04/xmlenc#sha512");

 private final String algoId;
 private final String xmlAlgoId;

 private DigestAlgo(String algoId, String xmlAlgoId) {
     this.algoId = algoId;
     this.xmlAlgoId = xmlAlgoId;
 }

 public String toString() {
     return this.algoId;
 }

 public String getAlgoId() {
     return this.algoId;
 }

 public String getXmlAlgoId() {
     return this.xmlAlgoId;
 }
}
