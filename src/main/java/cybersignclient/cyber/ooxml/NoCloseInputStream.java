package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.input.ProxyInputStream;

public class NoCloseInputStream extends ProxyInputStream {
 public NoCloseInputStream(InputStream proxy) {
     super(proxy);
 }

 public void close() throws IOException {
 }
}
