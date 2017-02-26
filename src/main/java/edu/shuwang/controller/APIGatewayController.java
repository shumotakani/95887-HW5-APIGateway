package edu.shuwang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.shuwang.form.QuoteNameForm;
import edu.shuwang.model.Quote;

@RestController
public class APIGatewayController {
	@Autowired
    private DiscoveryClient discoveryClient;

	@Autowired
	private APIGatewayAction apiGatewayAction;
	
    @RequestMapping("/api/quote/random")
    public Quote random() {
    	return apiGatewayAction.random();
    }
    
    @RequestMapping("/api/quote/list")
    public Quote[] list(String authorName) {
    	return apiGatewayAction.list(authorName);
    }
    
    @RequestMapping(value = "/api/quote", method = RequestMethod.POST)
    public void saveQuote(@RequestBody QuoteNameForm qnf) {
    	apiGatewayAction.saveQuote(qnf);
    }
}
