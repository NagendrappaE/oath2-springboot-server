package com.ece.oath2.client.utils;

import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jwt.SignedJWT;

@Service
public class ParService {

	private final String parEndpoint = "https://as1.altareq1.sandbox.apihub.openfinance.ae/par";

	@Autowired
	RestTemplate restTemplate;

	public String pushRequest(String clientId, String request, String client_assertion) {

		// Prepare headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Prepare request body
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("client_id", clientId);
		body.add("request", request);
		body.add("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
		body.add("client_assertion", client_assertion);

		// Create request entity
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);

		// Send request
		ResponseEntity<String> response = restTemplate.postForEntity(parEndpoint, requestEntity, String.class);

		System.out.println("PAR Response: " + response.getBody());
		return response.getBody();

	}

	public String singlePaymentPosting(String grantToken, String paymentJWS) throws ParseException {
		String url = "https://rs1.altareq1.sandbox.apihub.openfinance.ae/open-finance/payment/v2.0/payments";

		// Your JWT string
		// String jwt = "eyJhbGciOiJQUzI1NiIsImtpZCI6InpyS..."; // truncated for brevity

		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("application/jwt"));
		headers.set("x-fapi-financial-id", "CHANGEME0000000000");
		headers.set("x-fapi-customer-ip-address", "10.1.1.10");
		headers.set("x-fapi-interaction-id", UUID.randomUUID().toString());
		headers.set("Authorization", "Bearer " + grantToken);
		headers.set("x-idempotency-key", UUID.randomUUID().toString());

		// Request entity
		HttpEntity<String> entity = new HttpEntity<>(paymentJWS, headers);

		// Send POST request
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

		// Output response
		System.out.println("single payment Status: " + response.getStatusCode());
		System.out.println("single payment Body: " + response.getBody());
		SignedJWT signedJWT = SignedJWT.parse(response.getBody());

		// Get payload as JSON
		Map<String, Object> payloadJson = signedJWT.getPayload().toJSONObject();

		// Print or use as needed
		System.out.println("Decoded JSON: " + payloadJson);

		return response.getBody();

	}
}
