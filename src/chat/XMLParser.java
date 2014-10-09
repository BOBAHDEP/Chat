package chat;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser{
    private String fileName;

    public XMLParser(String fileName) {
        this.fileName = fileName;
    }

    private Document getDocument() {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();
            return builder.parse(new File(fileName));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String getValue(String attributeName) {
        NodeList methodNodes = getDocument().getElementsByTagName(attributeName);
        if (methodNodes.getLength() == 0 || methodNodes.getLength() > 1){
            System.err.println("Couldn't read XML");
            return null;
        }
        return methodNodes.item(0).getTextContent();
    }

    public List<String> getPasswordUser(){
        List<String> res = new ArrayList<String>();
        NodeList methodNodes = getDocument().getElementsByTagName("user");
        if (methodNodes.getLength() == 0){
            System.err.println("Couldn't read XML");
            return null;
        }else {
            for (int i = 0; i < methodNodes.getLength(); i++) {
                Node node = methodNodes.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Node nameAttrib = attributes.getNamedItem("name");
                String name = nameAttrib.getNodeValue();
                Node passAttrib = attributes.getNamedItem("password");
                String password = passAttrib.getNodeValue();
                res.add(name+":"+password);
            }
        }
        return res;
    }
}