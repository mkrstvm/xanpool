package com.mkrs.microservices.currencyexchangeservice;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@TestInstance(value = Lifecycle.PER_CLASS)
public class ccyExchangeControllerTest 
{
	private String access_key = "078b73c33bd0d123c7f7825f2ca9f8eb";
	
	@InjectMocks
	private CurrencyExchangeController ccyController;
	
	@BeforeAll
	public void Init()
	{
		MockitoAnnotations.openMocks(this);
	}
	
	
	@Mock
	private RestTemplate restTemplate;
	
	@Test
	public void testCcyControler()
	{	
		HashMap<String,Double> map = new HashMap<String,Double>();
		Date date = new Date(1232213);
		map.put("USD", 1.1234);
		String url = "http://data.fixer.io/api/latest?access_key=" + access_key +"&base="+ "EUR"+ "&symbols=" + "USD";
		CurrencyExchangeRate rate = new CurrencyExchangeRate(
				true,
				new BigDecimal("121231"),
				"EUR",
				date, 
				map);
		
		ccyController.StartCcyRateSubsciption();
		
		ResponseEntity<CurrencyExchangeRate> response = new ResponseEntity<CurrencyExchangeRate>(rate, HttpStatus.ACCEPTED);
		when(restTemplate.getForEntity(url, CurrencyExchangeRate.class)).thenReturn(response  );
		CurrencyExchangeRate actual = ccyController.GetExchangeRate("EUR", "USD");
		assertNotNull(actual);
	}

}
