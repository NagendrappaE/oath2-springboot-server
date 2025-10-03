package com.ece.oath2.client.utils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class RequestObjectService {

	@Autowired
	private RSAPrivateKey rsaPrivateKey; // load from your keystore/HSM
	private final String clientId = "https://rp.sandbox.directory.openfinance.ae/openid_relying_party/c2fd07a9-1341-4f42-99ae-dad1f95deaea";
	private final String audience = "https://auth1.altareq1.sandbox.apihub.openfinance.ae";
	private final String kid = "zrK7RjhGiWuepn4-FILVScwg4NfOOlawBZxXDRSx8pk";
	private final String jwksUrl = "https://keystore.sandbox.directory.openfinance.ae/233bcd1d-4216-4b3c-a362-9e4a9282bba7/application.jwks";
	private final String targetKid = "2G9OeXnW5PFN9YLVizgS18rjUI6I2DT18T6KKNxKkZU"; // OZNE API ENC Use your actual key
																					// ID

	private final String redirectUri = "https://sandbox-finaxis.bancify.me/oftf/redirection";

	public String randomNumber() {
		return UUID.randomUUID().toString();
	}

	public long issueTime() {
		return Instant.now().getEpochSecond();
	}

	public long expirationTime() {
		return Instant.now().plus(10, ChronoUnit.MINUTES).getEpochSecond();
	}

	public static String generateCodeVerifier() {
		SecureRandom sr = new SecureRandom();
		byte[] code = new byte[32]; // 32 bytes ~ 43 chars when base64url encoded
		sr.nextBytes(code);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
	}

	public static String generateCodeChallenge(String codeVerifier) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
		return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
	}

//JWS  Signing using Private key of RP
	public String build(String scope) throws Exception {

		String expiration = Instant.now().plus(30, ChronoUnit.DAYS).toString(); // outputs in ISO 8601 format

		// PKCE code verifier and challenge
		String codeVerifier = generateCodeVerifier();
		String codeChallenge = generateCodeChallenge(codeVerifier);

		// Map<String,String>test=new LinkedHashMap<String,String>();

		// Prepare authorization_details object
		Map<String, Object> onBehalfOf = new LinkedHashMap<String, Object>();
		onBehalfOf.put("TradingName", "Ozone");
		onBehalfOf.put("LegalName", "Ozone-CBUAE");
		onBehalfOf.put("IdentifierType", "Other");
		onBehalfOf.put("Identifier", "Identifier");

		Map<String, Object> openFinanceBilling = new LinkedHashMap<String, Object>();
		openFinanceBilling.put("UserType", "Retail");
		openFinanceBilling.put("Purpose", "AccountAggregation");

		Map<String, Object> consent = new LinkedHashMap<String, Object>();
		consent.put("ExpirationDateTime", expiration);
		consent.put("OnBehalfOf", onBehalfOf);
		consent.put("ConsentId", UUID.randomUUID().toString()); // or use your actual ConsentId
		consent.put("Permissions",
				Arrays.asList("ReadTransactionsCredits", "ReadAccountsBasic", "ReadBalances", "ReadTransactionsBasic",
						"ReadTransactionsDetail", "ReadDirectDebits", "ReadBeneficiariesDetail",
						"ReadBeneficiariesBasic", "ReadScheduledPaymentsBasic", "ReadScheduledPaymentsDetail",
						"ReadStandingOrdersBasic", "ReadStandingOrdersDetail", "ReadParty", "ReadPartyUserIdentity",
						"ReadProduct"));
		consent.put("OpenFinanceBilling", openFinanceBilling);

		Map<String, Object> webhook = new LinkedHashMap<String, Object>();
		webhook.put("Url", "https://rs1.altareq1.sandbox.apihub.openfinance.ae/mock-event-receiver");
		webhook.put("IsActive", true);

		Map<String, Object> subscription = new LinkedHashMap<String, Object>();
		subscription.put("Webhook", webhook);

		Map<String, Object> authorizationDetail = new LinkedHashMap<String, Object>();
		authorizationDetail.put("type", "urn:openfinanceuae:account-access-consent:v1.2");
		authorizationDetail.put("consent", consent);
		authorizationDetail.put("subscription", subscription);

		List<Map<String, Object>> authorizationDetailsList = Collections.singletonList(authorizationDetail);

		JWTClaimsSet claims = new JWTClaimsSet.Builder().issuer(clientId).audience(audience).claim("scope", scope) // "accounts
																													// openid"
				.claim("redirect_uri", redirectUri).claim("client_id", clientId).claim("nonce", randomNumber())
				.claim("state", randomNumber()).claim(JWTClaimNames.NOT_BEFORE, issueTime())
				.claim(JWTClaimNames.EXPIRATION_TIME, expirationTime()).claim("response_type", "code")
				.claim("code_challenge_method", "S256").claim("code_challenge", codeChallenge).claim("max_age", 3600)
				.claim("authorization_details", authorizationDetailsList).build();

		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.PS256).keyID(kid).type(JOSEObjectType.JWT).build();

		SignedJWT jwt = new SignedJWT(header, claims);
		jwt.sign(new RSASSASigner(rsaPrivateKey)); // rsaPrivateKey must be initialized elsewhere

		return jwt.serialize();
	}
	// JWS Signing using Private key of RP

	public String buildclient(String scope) throws Exception {

		JWTClaimsSet claims = new JWTClaimsSet.Builder().audience(audience).issuer(clientId).subject(clientId)
				.jwtID(randomNumber()) // Generates a random jti
				.claim(JWTClaimNames.EXPIRATION_TIME, expirationTime()).claim(JWTClaimNames.ISSUED_AT, issueTime())
				.build();

		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.PS256).type(JOSEObjectType.JWT).build();

		SignedJWT jwt = new SignedJWT(header, claims);
		jwt.sign(new RSASSASigner(rsaPrivateKey));

		return jwt.serialize();
	}

	// JWE --encrytpion using Public key of OZONE API
	public String buildClientJWE(String payload) throws Exception {

		// PostalAddress
		Map<String, Object> address = new LinkedHashMap<>();
		address.put("AddressType", "Business");
		address.put("Country", "AE");
		List<Map<String, Object>> postalAddress = List.of(address);

		// CreditorAgent
		Map<String, Object> creditorAgent = new LinkedHashMap<>();
		creditorAgent.put("SchemeName", "BICFI");
		creditorAgent.put("Identification", "10000109010101");
		creditorAgent.put("Name", "Mario International");
		creditorAgent.put("PostalAddress", postalAddress);

		// Creditor
		Map<String, Object> creditor = new LinkedHashMap<>();
		creditor.put("Name", "Mario International");

		// CreditorAccount
		Map<String, Object> creditorAccount = new LinkedHashMap<>();
		creditorAccount.put("SchemeName", "AccountNumber");
		creditorAccount.put("Identification", "10000109010101");
		Map<String, Object> nameObj = new LinkedHashMap<>();
		nameObj.put("en", "Mario International");
		creditorAccount.put("Name", nameObj);

		// Initiation
		Map<String, Object> initiation = new LinkedHashMap<>();
		initiation.put("CreditorAgent", creditorAgent);
		initiation.put("Creditor", creditor);
		initiation.put("CreditorAccount", creditorAccount);

		// Risk > DebtorIndicators
		Map<String, Object> userNameObj = new LinkedHashMap<>();
		userNameObj.put("en", "xx");
		Map<String, Object> debtorIndicators = new LinkedHashMap<>();
		debtorIndicators.put("UserName", userNameObj);

		// Risk > CreditorIndicators
		Map<String, Object> creditorIndicators = new LinkedHashMap<>();
		creditorIndicators.put("AccountType", "Retail");
		creditorIndicators.put("IsCreditorConfirmed", true);
		creditorIndicators.put("IsCreditorPrePopulated", true);
		creditorIndicators.put("TradingName", "xxx");

		// Risk
		Map<String, Object> risk = new LinkedHashMap<>();
		risk.put("DebtorIndicators", debtorIndicators);
		risk.put("CreditorIndicators", creditorIndicators);

		JWTClaimsSet claims = new JWTClaimsSet.Builder().audience(audience)
				.claim(JWTClaimNames.EXPIRATION_TIME, expirationTime()).claim(JWTClaimNames.ISSUED_AT, issueTime())
				.issuer(clientId).subject(clientId).jwtID(randomNumber()).claim("Initiation", initiation)
				.claim("Risk", risk).build();

		JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
				// .type(JOSEObjectType.JWT)
				.keyID(kid).build();

		// Parse JWKS from URL
		JWKSet jwkSet = JWKSet.load(new URL(jwksUrl));

		// Find key by kid
		JWK jwk = jwkSet.getKeyByKeyId(targetKid);
		if (jwk == null) {
			throw new IllegalArgumentException("No key found with kid: " + targetKid);
		}

		// Convert to RSAPublicKey
		if (!(jwk instanceof RSAKey)) {
			throw new IllegalArgumentException("Key is not RSA");
		}
		RSAPublicKey publicKey = ((RSAKey) jwk).toRSAPublicKey();

		JWSHeader headerS = new JWSHeader.Builder(JWSAlgorithm.PS256).keyID(kid).type(JOSEObjectType.JWT).build();

		SignedJWT jwt = new SignedJWT(headerS, claims);
		jwt.sign(new RSASSASigner(rsaPrivateKey));

		String signedJwt = jwt.serialize();

		JWEObject jweObject = new JWEObject(header, new Payload(signedJwt));

		// 5. Encrypt JWE (with the public key)
		jweObject.encrypt(new RSAEncrypter(publicKey));

		// 6. Get the compact JWE string
		String jweString = jweObject.serialize();

		return jweString;

	}

	public String buildPaymentAPIJWS(String consentId, String jweResponse) throws JOSEException {

		// Amount
		Map<String, Object> amount = new LinkedHashMap<>();
		amount.put("Amount", "150.00");
		amount.put("Currency", "AED");

		// Instruction
		Map<String, Object> instruction = new LinkedHashMap<>();
		instruction.put("Amount", amount);

		// OpenFinanceBilling
		Map<String, Object> openFinanceBilling = new LinkedHashMap<>();
		openFinanceBilling.put("Type", "Collection");

		// Data
		Map<String, Object> data = new LinkedHashMap<>();
		data.put("ConsentId", consentId);
		data.put("Instruction", instruction);
		data.put("OpenFinanceBilling", openFinanceBilling);
		data.put("PersonalIdentifiableInformation", jweResponse);
		data.put("PaymentPurposeCode", "ACM");
		data.put("DebtorReference", "TPP=a06154a7-fcb0-0472-be1c-21c8e5a74b6a,BIC=QW292P4TW8T");
		data.put("CreditorReference", "TPP=a06154a7-fcb0-0472-be1c-21c8e5a74b6a,BIC=QW292P4TW8T");

		// Message
		Map<String, Object> message = new LinkedHashMap<>();
		message.put("Data", data);

		
		
		JWSHeader headerJws = new JWSHeader.Builder(JWSAlgorithm.PS256).keyID(kid).build();

		JWTClaimsSet claimsbody = new JWTClaimsSet.Builder().audience(audience).issuer(clientId)
				.claim(JWTClaimNames.NOT_BEFORE, issueTime()).claim(JWTClaimNames.EXPIRATION_TIME, expirationTime())
				.claim(JWTClaimNames.ISSUED_AT, issueTime()).claim("message", message).build();

		SignedJWT jwt = new SignedJWT(headerJws, claimsbody);
		jwt.sign(new RSASSASigner(rsaPrivateKey)); // rsaPrivateKey must be initialized elsewhere

		return jwt.serialize();

	}

}
