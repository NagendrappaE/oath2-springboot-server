package com.app.authorisation.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/authorized")
@Slf4j
public class ClientController {

	@GetMapping("")
	public String authorized(Model model) {
		log.error("Inside authorized controller {}",model);
		return "authorized";
	}
}
