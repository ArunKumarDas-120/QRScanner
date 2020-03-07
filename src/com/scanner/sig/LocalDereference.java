package com.scanner.sig;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;

import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.XMLSignatureFactory;

public class LocalDereference implements URIDereferencer {

	@Override
	public Data dereference(URIReference uriReference, XMLCryptoContext context) throws URIReferenceException {
		Data result = null;
		String uri = uriReference.getURI();

		if (Objects.isNull(uri) || uri.startsWith("#") || uri.isEmpty() || uri.toUpperCase().startsWith("HTTP")
				|| uri.toUpperCase().startsWith("HTTPS"))
			result = XMLSignatureFactory.getInstance().getURIDereferencer().dereference(uriReference, context);
		else {
			try {
				FileInputStream fis = new FileInputStream(new File("F:\\test\\".concat(uri)));
				result = new OctetStreamData(fis, uriReference.getURI(), uriReference.getType());
			} catch (Exception e) {
				throw new URIReferenceException(e);
			}
		}

		return result;
	}

}
