package edu.shuwang.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import edu.shuwang.form.QuoteIdForm;
import edu.shuwang.form.QuoteNameForm;
import edu.shuwang.model.Author;
import edu.shuwang.model.Quote;

@Service
public class APIGatewayAction {
	
	@Value("${service.quote.random.uri}")
    private String quoteServerRandomUri;
    @Value("${service.quote.list.uri}")
    private String quoteServerListUri;
    @Value("${service.quote.save.uri}")
    private String quoteServerSaveUri;
    @Value("${service.author.id.uri}")
    private String authorServerIdUri;
	
    @HystrixCommand(fallbackMethod = "randomFallback")
    public Quote random() {
    	RestTemplate restTemplate = new RestTemplate();
        String uriRandom = quoteServerRandomUri;
        QuoteIdForm qf = restTemplate.getForObject(uriRandom, QuoteIdForm.class);
        
        String uriAuthorId = authorServerIdUri+qf.getAuthorId();
        Author a = restTemplate.getForObject(uriAuthorId, Author.class);
        
        Quote q = new Quote(qf.getText(), qf.getSource(), a);
        return q;
    }
    
    @HystrixCommand(fallbackMethod = "listFallback")
    public Quote[] list(String authorName) {
    	RestTemplate restTemplate = new RestTemplate();
        String uriList = quoteServerListUri+authorName;
        QuoteIdForm[] qfs = restTemplate.getForObject(uriList, QuoteIdForm[].class);
        
        Quote[] qs = new Quote[qfs.length];
        for (int i = 0; i < qfs.length; i++) {
        	String uriAuthorId = authorServerIdUri+qfs[i].getAuthorId();
        	Author a = restTemplate.getForObject(uriAuthorId, Author.class);
			qs[i] = new Quote(qfs[i].getText(), qfs[i].getSource(), a);
		}
        return qs;
    }
   
    @HystrixCommand(fallbackMethod = "saveFallback")
    public void saveQuote(QuoteNameForm qnf) {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	String uri = quoteServerSaveUri;
    	System.out.println(qnf.getText());
    	System.out.println(qnf.getSource());
    	System.out.println(qnf.getAuthorName());

    	ResponseEntity<Long> st = restTemplate.postForEntity(uri, qnf, Long.class);
    	System.out.println(st.getBody());
    }
    
    public Quote randomFallback() {
        return new Quote("Sorry but your action is failed","null",null);
    }
	
	public Quote[] listFallback(String query) {
		Quote[] qs = new Quote[1];
		qs[0] = new Quote("Sorry but your action is failed","null",null);
        return qs;
    }
	
	public void saveFallback() {
    }
}
