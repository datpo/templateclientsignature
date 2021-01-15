package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.util.LinkedList;
import java.util.List;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

public class RelationshipTransformParameterSpec implements TransformParameterSpec {
 private final List<String> sourceIds = new LinkedList();

 public RelationshipTransformParameterSpec() {
 }

 public void addRelationshipReference(String sourceId) {
     this.sourceIds.add(sourceId);
 }

 List<String> getSourceIds() {
     return this.sourceIds;
 }
}
