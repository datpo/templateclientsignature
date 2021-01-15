package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ProxyOutputStream;

public class NoCloseOutputStream extends ProxyOutputStream {
 public NoCloseOutputStream(OutputStream proxy) {
     super(proxy);
 }

 public void close() throws IOException {
 }
}

