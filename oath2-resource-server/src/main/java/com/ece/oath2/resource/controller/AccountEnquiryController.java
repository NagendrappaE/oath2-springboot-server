package com.ece.oath2.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//Just to test the resource server
@RestController
@RequestMapping("/account-enquiry")
public class AccountEnquiryController {

	@GetMapping("/public")
	public String publicEndpoint() {
		return "This is a public endpoint";
	}

	@GetMapping("/secure")
	public String secureEndpoint() {
		return "This is a secure endpoint, accessible only with 'account-enquiry:read'";
	}

}
