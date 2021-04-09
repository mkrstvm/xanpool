package com.mkrs.microservices.currencyexchangeservice;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RestController
public class CurrencyExchangeController 
{
	private HashMap<CcyPair, CurrencyExchangeRate> CachedCcyRates;
	
	private String access_key = "078b73c33bd0d123c7f7825f2ca9f8eb";
	
	private int Subscriptiontimer = 1;
	 
	 private List<CcyPair> CachedCcys = new ArrayList<CcyPair>()
	 {
		 {
			 add(new CcyPair("USD", "SGD"));
			 add(new CcyPair("SGD", "USD"));
			 add(new CcyPair("USD", "HKD"));
			 add(new CcyPair("HKD", "USD"));
		 }	 
		 
	};
	 
	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Bean
     public RestTemplate restTemplate() 
	 {
         return new RestTemplate();
     }
	 
	 
	 @PostConstruct
	 public void StartCcyRateSubsciption()
	 {
		 TimerTask task = new TimerTask() {
		        public void run() 
		        {
		            SubscribeForRates();
		        }
		    };
		    Timer timer = new Timer("Timer");
		    
		    long delay = Subscriptiontimer * 60 * 60 *1000;
		    timer.schedule(task, 0, delay);
	 }
	 
	 private void SubscribeForRates()
	 {		 
		 if(CachedCcyRates == null) CachedCcyRates = new HashMap<CcyPair, CurrencyExchangeRate>();
		 
		 CachedCcys.forEach( ccyPair -> 
		 {
			 CurrencyExchangeRate  newRate = GetExchangeRate(ccyPair.getBase(), ccyPair.getTerm());
			 
			 CachedCcyRates.put(ccyPair, newRate);
			 
		 });
		 ;
	 }
	 
	@GetMapping("/ccy-exchange/from/{from}/to/{to}")
	public CurrencyExchangeRate GetExchangeRate(@PathVariable String from, @PathVariable String to)
	{	
		CurrencyExchangeRate result = FromCache(from, to);
		if(result != null)return result;
		
		return CallExternalAPI(from, to);
	  
	}	
	
	private CurrencyExchangeRate CallExternalAPI(String base,String term)
	{
		String url = "http://data.fixer.io/api/latest?access_key=" + access_key +"&base="+ base+ "&symbols=" + term;
	    ResponseEntity<CurrencyExchangeRate> response =   restTemplate.getForEntity(url, CurrencyExchangeRate.class); 
	    CurrencyExchangeRate ccyRate = response.getBody();
	    ccyRate.setEnvironment("Dev");
	    ccyRate.setTerm(term);
	    return ccyRate;
	}
	
	private CurrencyExchangeRate FromCache(String base,String term)
	{
		if(CachedCcyRates == null  || CachedCcyRates.size() == 0)
		{
			return null;
		}
		CcyPair cachKey = new CcyPair(base, term);
		if(CachedCcyRates.containsKey(cachKey))
			return CachedCcyRates.get(cachKey);
		return null;
	}
}