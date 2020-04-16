import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class TomcatConnection {

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.trustStore", "F:/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		URL url = new URL("https://localhost:8443/manager");
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization",
				"Basic ".concat(Base64.getEncoder().encodeToString("root:root".getBytes())));
		try (Scanner sc = new Scanner(connection.getInputStream())) {
			while (sc.hasNextLine())
				System.out.println(sc.nextLine());
		}
	}
}
