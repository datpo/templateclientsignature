package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OOXMLTestDataStorage implements TemporaryDataStorage {
 private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 private Map<String, Serializable> attributes = new HashMap();

 public OOXMLTestDataStorage() {
 }

 public InputStream getTempInputStream() {
     byte[] data = this.outputStream.toByteArray();
     ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
     return inputStream;
 }

 public OutputStream getTempOutputStream() {
     return this.outputStream;
 }

 public Serializable getAttribute(String attributeName) {
     return (Serializable)this.attributes.get(attributeName);
 }

 public void setAttribute(String attributeName, Serializable attributeValue) {
     this.attributes.put(attributeName, attributeValue);
 }
}
