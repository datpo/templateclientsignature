package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public interface TemporaryDataStorage {
 OutputStream getTempOutputStream();

 InputStream getTempInputStream();

 void setAttribute(String var1, Serializable var2);

 Serializable getAttribute(String var1);
}
