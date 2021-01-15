package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.security.Provider;
import java.security.Security;

import org.apache.poi.poifs.crypt.dsig.services.RelationshipTransformService;

public class OOXMLProvider extends Provider {
 private static final long serialVersionUID = 1L;
 public static final String NAME = "OOXMLProvider";

 private OOXMLProvider() {
     super("OOXMLProvider", 1.0D, "OOXML Security Provider");
     this.put("TransformService.http://schemas.openxmlformats.org/package/2006/RelationshipTransform", RelationshipTransformService.class.getName());
     this.put("TransformService.http://schemas.openxmlformats.org/package/2006/RelationshipTransform MechanismType", "DOM");
 }

 public static void install() {
     Provider provider = Security.getProvider("OOXMLProvider");
     if (provider == null) {
         Security.addProvider(new OOXMLProvider());
     }

 }
}
