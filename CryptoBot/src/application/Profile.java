package application;

import org.knowm.xchange.currency.Currency;

import javafx.scene.control.ListView;

enum Exchange{ //List of supported exchanges that are available for profiles
	Cryptopia, 
	Default
}

public class Profile extends ListView<String> {
	/**
	 * A profile is an object that consists of an exchange, the user's API Key, API Secret,
	 * and the two coins of the market that is being traded on.
	 */
	Exchange exchange;
	String apiKey;
	String apiSecret;
	Currency coinOne;
	Currency coinTwo;
	
	public Profile(Exchange exchange, String apiKey, String apiSecret, Currency coinOne, Currency coinTwo){
		this.exchange = exchange;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.coinOne = coinOne;
		this.coinTwo = coinTwo;
	}
	
	Exchange getExchange(){
		return exchange;
	}
	
	String getApiKey(){
		return apiKey;
	}
	
	String getApiSecret(){
		return apiSecret;
	}
	
	Currency getCoinOne(){
		return coinOne;
	}
	
	Currency getCoinTwo(){
		return coinTwo;
	}
	
	void setExchange(Exchange exchange){
		this.exchange = exchange;
	}
	
	void setApiKey(String apiKey){
		this.apiKey = apiKey;
	}
		
	void setApiSecret(String apiSecret){
		this.apiSecret = apiSecret;
	}
	
	void setCoinOne(Currency coinOne){
		this.coinOne = coinOne;
	}
	
	void setCoinTwo(Currency coinTwo){
		this.coinTwo = coinTwo;
	}
	
}


