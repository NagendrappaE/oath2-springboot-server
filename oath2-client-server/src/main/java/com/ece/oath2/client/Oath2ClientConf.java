package com.ece.oath2.client;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Oath2ClientConf {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable() // CSRF can interfere with PAR redirect
				.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll() // allow your PAR redirect
																						// endpoint
						.anyRequest().authenticated() // protect other endpoints
				);

		return http.build();
	}

	@Bean
	public RSAPrivateKey rsaPrivateKey(@Value("${rsa.privatekey.path}") String filePath) throws Exception {
		// Read all bytes from PEM file
		String privateKeyPEM = Files.readString(Paths.get(filePath));

		// Remove PEM headers/footers and newlines
		privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");

		// Decode Base64
		byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);

		// Generate PrivateKey from PKCS#8 spec
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = kf.generatePrivate(keySpec);

		// Cast to RSAPrivateKey
		return (RSAPrivateKey) privateKey;
	}

	/*
	 * @Value("${trust.store}") private Resource trustStore;
	 * 
	 * @Value("${trust.store.password}") private String trustStorePassword;
	 */
	@Bean
	public RestTemplate restTemplate() {

		return new RestTemplate();
	}

	/*
	 * @Bean public RestTemplate restTemplate() throws KeyManagementException,
	 * NoSuchAlgorithmException, KeyStoreException, CertificateException,
	 * MalformedURLException, IOException, java.security.cert.CertificateException,
	 * UnrecoverableKeyException {
	 * 
	 * KeyStore keyStore = KeyStore.getInstance("PKCS12"); try (FileInputStream fis
	 * = new FileInputStream(
	 * "/home/nagendrappae/Documents/openFinance/altareqKeystore.jks")) {
	 * keyStore.load(fis, "Flux@123".toCharArray()); }
	 * 
	 * SSLContext sslContext = new SSLContextBuilder()
	 * .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
	 * .loadKeyMaterial(keyStore, "Flux@123".toCharArray()) .build();
	 * 
	 * 
	 * 
	 * 
	 * 
	 * SSLConnectionSocketFactory sslConFactory = new
	 * SSLConnectionSocketFactory(sslContext); HttpClientConnectionManager cm =
	 * PoolingHttpClientConnectionManagerBuilder.create()
	 * .setSSLSocketFactory(sslConFactory).build(); CloseableHttpClient httpClient =
	 * HttpClients.custom().setConnectionManager(cm).build();
	 * ClientHttpRequestFactory requestFactory = new
	 * HttpComponentsClientHttpRequestFactory(httpClient); return new
	 * RestTemplate(requestFactory); }
	 */
}
