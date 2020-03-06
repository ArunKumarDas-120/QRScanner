package com.scanner.sig;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;

public class LocalDereference implements URIDereferencer {

	@Override
	public Data dereference(URIReference uriReference, XMLCryptoContext context) throws URIReferenceException {
		try {
			FileInputStream fis = new FileInputStream(new File("F:\\test\\employeesalary.xml"));
			return new OctetStreamData(fis, uriReference.getURI(), uriReference.getType());
		} catch (Exception e) {
			throw new URIReferenceException(e);
		}

	}

}
