
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class HttpsClient {

	// openConnection -> getOutputStream -> write -> getInputStream -> read

	private static List<String> cookies;

	public static void main(String[] args) throws IOException {

		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		System.setProperty("javax.net.ssl.trustStoreType", "jks");
		System.setProperty("javax.net.ssl.keyStore", "F:/XBG000001.p12");
		System.setProperty("javax.net.ssl.trustStore", "F:/gridserver.keystore");
		System.setProperty("javax.net.ssl.keyStorePassword", "yjhciz63");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		URL url = new URL("https://uat.mnp2.org.uk/xml");
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestProperty("Ocp-Apim-Subscription-Key", "<>");
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "text/xml; charset=ISO-8859-1");
		connection.setRequestProperty("Content-Language", "en-GB");
		connection.setRequestProperty("SOAPAction", "https://uat.mnp2.org.uk/xml");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Language", "en-US");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<!DOCTYPE mnp_request SYSTEM \"https://uat.mnp2.org.uk/dtd/mnprequest.dtd\">" + "<mnp_request>"
				+ "<logon>" + "<authenticator>" + "<username>esnAdmin1_xbg</username>" + "<password>aaaaaaa$</password>"
				+ "</authenticator>" + "</logon>" + "</mnp_request>";
		try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
			writer.write(xml);
			writer.close();

		}
		try (Scanner sc = new Scanner(connection.getInputStream())) {
			while (sc.hasNextLine())
				System.out.println(sc.nextLine());
		}
		connection.getContent();
		List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
		for (HttpCookie cookie : cookies) {
			System.out.println(cookie.getDomain());
			System.out.println(cookie);
		}

	}

}
