package com.ece.oath2.client.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api")
public class CAAPController {

	String clientid = "https://rp.sandbox.directory.openfinance.ae/openid_relying_party/c2fd07a9-1341-4f42-99ae-dad1f95deaea";
	String authUrlo = "https://auth1.altareq1.sandbox.apihub.openfinance.ae/auth";

	@Autowired
	RequestObjectService requestObjectService;

	@Autowired
	ParService parService;

	@GetMapping("/login/caap")
	public void loginWithCaap(HttpServletResponse response) throws Exception {

		String requestObject = requestObjectService.build("accounts openid");

		String buildclient = requestObjectService.buildclient("openid accounts caap");

		System.out.println("buildclient Object: " + buildclient);

		System.out.println("Request Object: " + requestObject);

		String parResponse = parService.pushRequest(clientid, requestObject, buildclient);
		String requestUri = extractRequestUri(parResponse); // parse JSON

		String authUrl = authUrlo + "?client_id=" + clientid + "&response_type=code&scope=openid&request_uri="
				+ requestUri;

		response.sendRedirect(authUrl);
	}

	public String extractRequestUri(String parJson) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ParResponse response = mapper.readValue(parJson, ParResponse.class);
		return response.getRequest_uri();
	}

	@GetMapping("/login/payment/{grantToken}/{consentid}")
	public void paymentFlow(HttpServletResponse response, @PathVariable("grantToken") String grantToken,@PathVariable("consentid") String consentid)
			throws Exception {

		String jweResponse = requestObjectService.buildClientJWE("Test");

		System.out.println("buildclient jweResponse: " + jweResponse);
		String paymentJWS = requestObjectService.buildPaymentAPIJWS(consentid,
				jweResponse);

		System.out.println("buildclient paymentJWS: " + paymentJWS);
		
		String parResponse = parService.singlePaymentPosting(grantToken, paymentJWS);

	}
}
