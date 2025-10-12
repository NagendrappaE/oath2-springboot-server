package com.app.authorisation.server.conf;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegisteredClientInitializer {

	@Autowired
	private RegisteredClientRepository jdbcRegisteredClientRepository;

	@PostConstruct public void init() {
	  
	  log.info("Initializing Registered Clients...");
	  
		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("emandate-switch").clientSecret("{noop}secret") // Use {bcrypt} for production

				.scope("account-enquiry:read").redirectUri("http://127.0.0.1:8080/authorized")
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(12))
						.refreshTokenTimeToLive(Duration.ofHours(12)).build())

				.build();
	  
	  RegisteredClient publicClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("public-client")
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("http://127.0.0.1:8080/authorized")
				.scope(OidcScopes.OPENID)
				.scope("iblogin-scope")
				.scope(OidcScopes.PROFILE)
				.clientSettings(ClientSettings.builder()
					.requireAuthorizationConsent(true)
					.requireProofKey(true)
					.build()
				)
				.build();
	  
	  
	
	// jdbcRegisteredClientRepository.save(registeredClient); 
	  
	}

}
