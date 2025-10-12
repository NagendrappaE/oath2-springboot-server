package com.app.authorisation.server.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/bank-login")
public class BankLoginController {

	@GetMapping("")
	public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {

		return "bank-login";
	}

//	@PostMapping("/bank-login")
//	public String processLogin(@RequestParam String username, @RequestParam String password,
//			HttpServletRequest request) {
//
//		// Load user from database
//		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//		// Check password
//		if (passwordEncoder.matches(password, userDetails.getPassword())) {
//			// Create authentication token
//			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, password,
//					userDetails.getAuthorities());
//
//			// Set authentication in security context
//			SecurityContextHolder.getContext().setAuthentication(auth);
//
//			// Optionally: manually create session
//			HttpSession session = request.getSession(true);
//			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
//					SecurityContextHolder.getContext());
//
//			// Redirect to original requested page or default
//			return "redirect:/";
//		} else {
//			// Invalid login
//			return "redirect:/bank-login?error";
//		}
//
//	}

}
