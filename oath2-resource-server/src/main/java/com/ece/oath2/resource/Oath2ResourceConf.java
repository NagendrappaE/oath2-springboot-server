package com.ece.oath2.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class Oath2ResourceConf {
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests(authz -> authz
	                .requestMatchers("/account-enquiry/public").permitAll()
	                .requestMatchers("/account-enquiry/secure").hasAuthority("SCOPE_account-enquiry:read")
	                .anyRequest().authenticated()
	            )
	            .oauth2ResourceServer(oauth2 -> oauth2
	                .jwt()
	            );
	        return http.build();
	    }
}
