package cybersignclient.cyber.ooxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Manifest;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignContext;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.xpath.XPathAPI;
import org.jcp.xml.dsig.internal.dom.DOMReference;
import org.jcp.xml.dsig.internal.dom.DOMSignedInfo;
import org.jcp.xml.dsig.internal.dom.DOMXMLSignature;
import org.jcp.xml.dsig.internal.dom.XMLDSigRI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class AbstractXmlSignatureService implements SignatureService {
    private static final String SIGNATURE_ID_ATTRIBUTE = "signature-id";
    private final List signatureAspects = new LinkedList();

    public AbstractXmlSignatureService() {
    }

    protected void addSignatureAspect(SignatureAspect signatureAspect) {
        this.signatureAspects.add(signatureAspect);
    }

    public String getSignatureDigestAlgorithm() {
        return "SHA-1";
    }

    protected List getServiceDigestInfos() {
        return new LinkedList();
    }

    protected Document getEnvelopingDocument() throws ParserConfigurationException, IOException, SAXException {
        return null;
    }

    protected List getReferenceUris() {
        return new LinkedList();
    }

    protected List getReferences() {
        return new LinkedList();
    }

    protected URIDereferencer getURIDereferencer() {
        return null;
    }

    protected String getSignatureDescription() {
        return "XML Signature";
    }

    protected abstract TemporaryDataStorage getTemporaryDataStorage();

    protected abstract OutputStream getSignedDocumentOutputStream();

    public DigestInfo preSign(List digestInfos, List signingCertificateChain) throws NoSuchAlgorithmException {
        String digestAlgo = this.getSignatureDigestAlgorithm();

        byte[] digestValue;
        try {
            digestValue = this.getXmlSignatureDigestValue(digestAlgo, digestInfos);
        } catch (Exception var6) {
            throw new RuntimeException("XML signature error: " + var6.getMessage(), var6);
        }

        String description = this.getSignatureDescription();
        return new DigestInfo(digestValue, digestAlgo, description);
    }

    protected void postSign(Element element, List list) {
    }

    public void postSign(byte[] signatureValue, List signingCertificateChain) {
        TemporaryDataStorage temporaryDataStorage = this.getTemporaryDataStorage();
        InputStream documentInputStream = temporaryDataStorage.getTempInputStream();
        String signatureId = (String)temporaryDataStorage.getAttribute("signature-id");

        Document document;
        try {
            document = this.loadDocument(documentInputStream);
        } catch (Exception var15) {
            throw new RuntimeException("DOM error: " + var15.getMessage(), var15);
        }

        Element nsElement = document.createElement("ns");
        nsElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");

        Element signatureElement;
        try {
            signatureElement = (Element)XPathAPI.selectSingleNode(document, "//ds:Signature[@Id='" + signatureId + "']", nsElement);
        } catch (TransformerException var14) {
            throw new RuntimeException("XPATH error: " + var14.getMessage(), var14);
        }

        if (signatureElement == null) {
            throw new RuntimeException("ds:Signature not found for @Id: " + signatureId);
        } else {
            NodeList signatureValueNodeList = signatureElement.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
            Element signatureValueElement = (Element)signatureValueNodeList.item(0);
            signatureValueElement.setTextContent(DatatypeConverter.printBase64Binary(signatureValue));
            this.postSign(signatureElement, signingCertificateChain);
            OutputStream signedDocumentOutputStream = this.getSignedDocumentOutputStream();
            if (signedDocumentOutputStream == null) {
                throw new IllegalArgumentException("signed document output stream is null");
            } else {
                try {
                    this.writeDocument(document, signedDocumentOutputStream);
                } catch (Exception var13) {
                    throw new RuntimeException("error writing the signed XML document: " + var13.getMessage(), var13);
                }
            }
        }
    }

    protected String getCanonicalizationMethod() {
        return "http://www.w3.org/2001/10/xml-exc-c14n#";
    }

    public byte[] getXmlSignatureDigestValue(String digestAlgo, List digestInfos) throws ParserConfigurationException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, XMLSignatureException, TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
        byte[] octets = this.getXmlSignatureDigestData(digestAlgo, digestInfos);
        return octets;
    }

    // hash
    public byte[] getXmlSignatureDigestData(String digestAlgo, List digestInfos) throws ParserConfigurationException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, XMLSignatureException, TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
        Document document = this.getEnvelopingDocument();
        if (document == null) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
        }

        Key key = new Key() {
            private static final long serialVersionUID = 1L;

            public String getAlgorithm() {
                return null;
            }

            public byte[] getEncoded() {
                return null;
            }

            public String getFormat() {
                return null;
            }
        };
        XMLSignContext xmlSignContext = new DOMSignContext(key, document);
        URIDereferencer uriDereferencer = this.getURIDereferencer();
        if (uriDereferencer != null) {
            xmlSignContext.setURIDereferencer(uriDereferencer);
        }

        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM", new XMLDSigRI());
        List references = new LinkedList();
        this.addDigestInfosAsReferences(digestInfos, signatureFactory, references);
        List serviceDigestInfos = this.getServiceDigestInfos();
        this.addDigestInfosAsReferences(serviceDigestInfos, signatureFactory, references);
        this.addReferenceIds(signatureFactory, xmlSignContext, references);
        this.addReferences(signatureFactory, references);
        String signatureId = "xmldsig-" + UUID.randomUUID().toString();
        List objects = new LinkedList();
        Iterator i$ = this.signatureAspects.iterator();

        while(i$.hasNext()) {
            SignatureAspect signatureAspect = (SignatureAspect)i$.next();
            signatureAspect.preSign(signatureFactory, document, signatureId, references, objects);
        }

        SignatureMethod signatureMethod = signatureFactory.newSignatureMethod(this.getSignatureMethod(digestAlgo), (SignatureMethodParameterSpec)null);
        CanonicalizationMethod canonicalizationMethod = signatureFactory.newCanonicalizationMethod(this.getCanonicalizationMethod(), (C14NMethodParameterSpec)null);
        SignedInfo signedInfo = signatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod, references);
        String signatureValueId = signatureId + "-signature-value";
        XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, (KeyInfo)null, objects, signatureId, signatureValueId);
        DOMXMLSignature domXmlSignature = (DOMXMLSignature)xmlSignature;
        Node documentNode = document.getDocumentElement();
        if (documentNode == null) {
            documentNode = document;
        }

        String dsPrefix = null;
        domXmlSignature.marshal((Node)documentNode, (String)dsPrefix, (DOMCryptoContext)xmlSignContext);
        Iterator i$1 = objects.iterator();

        label56:
        while(i$1.hasNext()) {
            XMLObject object = (XMLObject)i$1.next();
            List objectContentList = object.getContent();
            Iterator i1$ = objectContentList.iterator();

            while(true) {
                XMLStructure objectContent;
                do {
                    if (!i1$.hasNext()) {
                        continue label56;
                    }

                    objectContent = (XMLStructure)i1$.next();
                } while(!(objectContent instanceof Manifest));

                Manifest manifest = (Manifest)objectContent;
                List manifestReferences = manifest.getReferences();
                Iterator i2$ = manifestReferences.iterator();

                while(i2$.hasNext()) {
                    Reference manifestReference = (Reference)i2$.next();
                    if (manifestReference.getDigestValue() == null) {
                        DOMReference manifestDOMReference = (DOMReference)manifestReference;
                        manifestDOMReference.digest(xmlSignContext);
                    }
                }
            }
        }

        List signedInfoReferences = signedInfo.getReferences();
        Iterator i$11 = signedInfoReferences.iterator();

        while(i$11.hasNext()) {
            Reference signedInfoReference = (Reference)i$11.next();
            DOMReference domReference = (DOMReference)signedInfoReference;
            if (domReference.getDigestValue() == null) {
                domReference.digest(xmlSignContext);
            }
        }

        TemporaryDataStorage temporaryDataStorage = this.getTemporaryDataStorage();
        OutputStream tempDocumentOutputStream = temporaryDataStorage.getTempOutputStream();
        this.writeDocument(document, tempDocumentOutputStream);
        temporaryDataStorage.setAttribute("signature-id", signatureId);
        DOMSignedInfo domSignedInfo = (DOMSignedInfo)signedInfo;
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        domSignedInfo.canonicalize(xmlSignContext, dataStream);
        byte[] octets = dataStream.toByteArray();
        return octets;
    }

    private void addReferenceIds(XMLSignatureFactory signatureFactory, XMLSignContext xmlSignContext, List references) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, XMLSignatureException {
        List referenceUris = this.getReferenceUris();
        if (referenceUris != null) {
            DigestMethod digestMethod = signatureFactory.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", (DigestMethodParameterSpec)null);
            Iterator i$ = referenceUris.iterator();

            while(i$.hasNext()) {
                String referenceUri = (String)i$.next();
                Reference reference = signatureFactory.newReference(referenceUri, digestMethod);
                references.add(reference);
            }

        }
    }

    private void addReferences(XMLSignatureFactory xmlSignatureFactory, List references) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        List referenceInfos = this.getReferences();
        if (referenceInfos != null) {
            if (!referenceInfos.isEmpty()) {
                DigestMethod digestMethod = xmlSignatureFactory.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", (DigestMethodParameterSpec)null);
                Iterator i$ = referenceInfos.iterator();

                while(i$.hasNext()) {
                    AbstractXmlSignatureService.ReferenceInfo referenceInfo = (AbstractXmlSignatureService.ReferenceInfo)i$.next();
                    List transforms = new LinkedList();
                    if (referenceInfo.getTransform() != null) {
                        Transform transform = xmlSignatureFactory.newTransform(referenceInfo.getTransform(), (TransformParameterSpec)null);
                        transforms.add(transform);
                    }

                    Reference reference = xmlSignatureFactory.newReference(referenceInfo.getUri(), digestMethod, transforms, (String)null, (String)null);
                    references.add(reference);
                }

            }
        }
    }

    private void addDigestInfosAsReferences(List digestInfos, XMLSignatureFactory signatureFactory, List references) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, MalformedURLException {
        if (digestInfos != null) {
            Iterator i$ = digestInfos.iterator();

            while(i$.hasNext()) {
                DigestInfo digestInfo = (DigestInfo)i$.next();
                byte[] documentDigestValue = digestInfo.digestValue;
                DigestMethod digestMethod = signatureFactory.newDigestMethod(this.getXmlDigestAlgo(digestInfo.digestAlgo), (DigestMethodParameterSpec)null);
                String uri = FilenameUtils.getName((new File(digestInfo.description)).toURI().toURL().getFile());
                Reference reference = signatureFactory.newReference(uri, digestMethod, (List)null, (String)null, (String)null, documentDigestValue);
                references.add(reference);
            }

        }
    }

    private String getXmlDigestAlgo(String digestAlgo) {
        if ("SHA-1".equals(digestAlgo)) {
            return "http://www.w3.org/2000/09/xmldsig#sha1";
        } else if ("SHA-256".equals(digestAlgo)) {
            return "http://www.w3.org/2001/04/xmlenc#sha256";
        } else if ("SHA-512".equals(digestAlgo)) {
            return "http://www.w3.org/2001/04/xmlenc#sha512";
        } else {
            throw new RuntimeException("unsupported digest algo: " + digestAlgo);
        }
    }

    private String getSignatureMethod(String digestAlgo) {
        if (digestAlgo == null) {
            throw new RuntimeException("digest algo is null");
        } else if ("SHA-1".equals(digestAlgo)) {
            return "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
        } else if ("SHA-256".equals(digestAlgo)) {
            return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
        } else if ("SHA-512".equals(digestAlgo)) {
            return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
        } else if ("SHA-384".equals(digestAlgo)) {
            return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
        } else if ("RIPEMD160".equals(digestAlgo)) {
            return "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160";
        } else {
            throw new RuntimeException("unsupported sign algo: " + digestAlgo);
        }
    }

    protected void writeDocument(Document document, OutputStream documentOutputStream) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        this.writeDocumentNoClosing(document, documentOutputStream);
        documentOutputStream.close();
    }

    protected void writeDocumentNoClosing(Document document, OutputStream documentOutputStream) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        this.writeDocumentNoClosing(document, documentOutputStream, false);
    }

    protected void writeDocumentNoClosing(Document document, OutputStream documentOutputStream, boolean omitXmlDeclaration) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        NoCloseOutputStream outputStream = new NoCloseOutputStream(documentOutputStream);
        Result result = new StreamResult(outputStream);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        if (omitXmlDeclaration) {
            xformer.setOutputProperty("omit-xml-declaration", "yes");
        }

        Source source = new DOMSource(document);
        xformer.transform(source, result);
    }

    protected Document loadDocument(InputStream documentInputStream) throws ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = new InputSource(documentInputStream);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputSource);
        return document;
    }

    protected Document loadDocumentNoClose(InputStream documentInputStream) throws ParserConfigurationException, SAXException, IOException {
        NoCloseInputStream noCloseInputStream = new NoCloseInputStream(documentInputStream);
        InputSource inputSource = new InputSource(noCloseInputStream);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputSource);
        return document;
    }

    public static class ReferenceInfo {
        private final String uri;
        private final String transform;

        public String getUri() {
            return this.uri;
        }

        public String getTransform() {
            return this.transform;
        }

        public ReferenceInfo(String uri, String transform) {
            this.uri = uri;
            this.transform = transform;
        }

        public ReferenceInfo(String uri) {
            this(uri, (String)null);
        }
    }
}

