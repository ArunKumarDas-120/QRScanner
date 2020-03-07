package com.scanner.sig;

public class Main {

	public static void main(String[] args) throws Exception {

		XmlDigitalSignatureGenerator x = new XmlDigitalSignatureGenerator();
		x.generateXMLDigitalSignature("employeesalary.xml", "/test/signed.xml", "/test/privatekey.key",
				"/test/publickey.key");

		System.out.println(
				XmlDigitalSignatureVerifier.isXmlDigitalSignatureValid("/test/signed.xml", "/test/publickey.key"));

	}

}
