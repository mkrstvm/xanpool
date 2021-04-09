package com.mkrs.microservices.currencyexchangeservice;

public class CcyPair 
{
	private String base;
	private String term;
	
	public CcyPair(String base, String term) 
	{
		this.base = base;
		this.term = term;
				
	}
	
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	
	@Override
	public boolean equals(Object o)
	{
		CcyPair pair = (CcyPair)o;
		return base == pair.getBase() &&
				term == pair.getTerm();
	}

}
