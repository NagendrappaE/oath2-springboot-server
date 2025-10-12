package com.app.authorisation.server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppAuthorisationServerApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(AppAuthorisationServerApplication.class, args);
		
		String codeverifier=UUID.randomUUID().toString();
		
		System.out.println("codeverifier: "+codeverifier);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(codeverifier.getBytes(StandardCharsets.US_ASCII));
		String codechallenge= Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
		
		System.out.println("codechallenge: "+codechallenge);

	}

}
