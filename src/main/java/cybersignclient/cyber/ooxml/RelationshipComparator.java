package cybersignclient.cyber.ooxml;

//
//Source code recreated from a .class file by IntelliJ IDEA
//(powered by FernFlower decompiler)
//



import java.util.Comparator;
import org.w3c.dom.Element;

public class RelationshipComparator implements Comparator<Element> {
 public RelationshipComparator() {
 }

 public int compare(Element element1, Element element2) {
     String id1 = element1.getAttribute("Id");
     String id2 = element2.getAttribute("Id");
     return id1.compareTo(id2);
 }
}
