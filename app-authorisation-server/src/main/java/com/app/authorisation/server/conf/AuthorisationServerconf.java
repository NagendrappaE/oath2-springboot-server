
package com.app.authorisation.server.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration(proxyBeanMethods = false)

public class AuthorisationServerconf {

	@Bean
	RegisteredClientRepository jdbcRegisteredClientRepository(@Autowired JdbcTemplate jdbcTemplate) {
		return new JdbcRegisteredClientRepository(jdbcTemplate);
	}
	//to save the token Details

	@Bean
	JdbcOAuth2AuthorizationService JdbcOAuth2AuthorizationService(@Autowired JdbcTemplate jdbcTemplate,@Autowired RegisteredClientRepository  jdbcRegisteredClientRepository) {
		return new JdbcOAuth2AuthorizationService(jdbcTemplate,jdbcRegisteredClientRepository);
	}
	
	//to save the consent Details

	
	@Bean
	JdbcOAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService(@Autowired JdbcTemplate jdbcTemplate,
			@Autowired RegisteredClientRepository jdbcRegisteredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, jdbcRegisteredClientRepository);
	}
	
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
				.authorizationServer();

		http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher()).with(authorizationServerConfigurer,
				(authorizationServer) -> authorizationServer.oidc(Customizer.withDefaults()) // Enable OpenID Connect
																								// 1.0
		).authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
				// Redirect to the login page when not authenticated from the
				// authorization endpoint
				.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/bank-login"),
						new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

		return http.cors(Customizer.withDefaults()).build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
				// Form login handles the redirect to the login page from the
				// authorization server filter chain
				.formLogin(log->log.loginPage("/bank-login").permitAll());

		return http.cors(Customizer.withDefaults()).build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addAllowedOrigin("*");
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
