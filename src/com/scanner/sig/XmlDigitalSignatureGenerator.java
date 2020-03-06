package com.scanner.sig;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class XmlDigitalSignatureGenerator {

	/**
	 * Method used to get the KeyInfo
	 *
	 * @param xmlSigFactory
	 * @param publicKeyPath
	 * @return KeyInfo
	 */
	private KeyInfo getKeyInfo(XMLSignatureFactory xmlSigFactory, String publicKeyPath) {
		KeyInfo keyInfo = null;
		KeyValue keyValue = null;
		PublicKey publicKey = new KryptoUtil().getStoredPublicKey(publicKeyPath);
		KeyInfoFactory keyInfoFact = xmlSigFactory.getKeyInfoFactory();

		try {
			keyValue = keyInfoFact.newKeyValue(publicKey);
		} catch (KeyException ex) {
			ex.printStackTrace();
		}
		keyInfo = keyInfoFact.newKeyInfo(Collections.singletonList(keyValue));
		return keyInfo;
	}

	/*
	 * Method used to store the signed XMl document
	 */
	private void storeSignedDoc(Document doc, String destnSignedXmlFilePath) {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer trans = null;
		try {
			trans = transFactory.newTransformer();
		} catch (TransformerConfigurationException ex) {
			ex.printStackTrace();
		}
		try {
			StreamResult streamRes = new StreamResult(new File(destnSignedXmlFilePath));
			trans.transform(new DOMSource(doc), streamRes);
		} catch (TransformerException ex) {
			ex.printStackTrace();
		}
		System.out.println("XML file with attached digital signature generated successfully ...");
	}

	/**
	 * Method used to attach a generated digital signature to the existing document
	 *
	 * @param originalXmlFilePath
	 * @param destnSignedXmlFilePath
	 * @param privateKeyFilePath
	 * @param publicKeyFilePath
	 * @throws ParserConfigurationException
	 */
	public void generateXMLDigitalSignature(String originalXmlFilePath, String destnSignedXmlFilePath,
			String privateKeyFilePath, String publicKeyFilePath) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true); // must be set
		Document doc = dbf.newDocumentBuilder().newDocument();
		XMLSignatureFactory xmlSigFactory = XMLSignatureFactory.getInstance("DOM");
		PrivateKey privateKey = new KryptoUtil().getStoredPrivateKey(privateKeyFilePath);

		Reference ref = null;
		SignedInfo signedInfo = null;
		try {
			ref = xmlSigFactory.newReference("employeesalary.xml",
					xmlSigFactory.newDigestMethod(DigestMethod.SHA1, null));
			signedInfo = xmlSigFactory.newSignedInfo(
					xmlSigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
							(C14NMethodParameterSpec) null),
					xmlSigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (InvalidAlgorithmParameterException ex) {
			ex.printStackTrace();
		}
		// Pass the Public Key File Path
		KeyInfo keyInfo = getKeyInfo(xmlSigFactory, publicKeyFilePath);
		// Create a new XML Signature
		XMLSignature xmlSignature = xmlSigFactory.newXMLSignature(signedInfo, keyInfo);
		try {
			DOMSignContext domSignCtx = new DOMSignContext(privateKey, doc);
			LocalDereference derf = new LocalDereference();
			domSignCtx.setURIDereferencer(derf);
			xmlSignature.sign(domSignCtx);
		} catch (MarshalException ex) {
			ex.printStackTrace();
		} catch (XMLSignatureException ex) {
			ex.printStackTrace();
		}
		// Store the digitally signed document inta a location
		storeSignedDoc(doc, destnSignedXmlFilePath);
	}
}
