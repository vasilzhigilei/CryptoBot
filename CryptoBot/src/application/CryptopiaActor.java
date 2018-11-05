package application;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.cryptopia.Cryptopia;
import org.knowm.xchange.cryptopia.CryptopiaExchange;
import org.knowm.xchange.cryptopia.service.CryptopiaAccountService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;

import com.sun.javafx.scene.paint.GradientUtils.Parser;

//import ch.qos.logback.core.subst.Token.Type;

public class CryptopiaActor { // original file in xchange examples cryptopia file

	public static Currency coinOne = Currency.BTC; 
	public static Currency coinTwo = Currency.ETH; //set the two coins
	
	public static CurrencyPair currencyPair = new CurrencyPair(coinTwo, coinOne); //has to be reversed for Cryptopia
			
	static BigDecimal coinOneBalance; static BigDecimal coinTwoBalance;
  
	
	public static BigDecimal bidPrice; public static BigDecimal askPrice; //market data start
	public static BigDecimal highPrice; public static BigDecimal lowPrice;
	public static BigDecimal lastPrice; public static BigDecimal volume; //market data stop
	
	public static BigDecimal amountTrading = new BigDecimal(0.01000000); //total amount ETH being traded, needs to be more than BTC 0.0005 sat
	
	
	//Exchange global setup
	ExchangeSpecification exchangeSpecification = new CryptopiaExchange().getDefaultExchangeSpecification();
	Exchange cryptopiaExchange;
	
	static AccountService accountService; //usage of static will be tested
	static MarketDataService marketService;
	static TradeService tradeService;
	//Exchange global setup end
	
	public CryptopiaActor(String apiKey, String apiSecret, Currency coinOne, Currency coinTwo){
		/**
		 * Defines the CryptopiaActor. The "actor" consists of an API Key, API Secret, and the
		 * two coins of the market being traded on.
		 */
		exchangeSpecification.setApiKey(apiKey); // API Key from Cryptopia
		exchangeSpecification.setSecretKey(apiSecret); // API Secret from Cryptopia
		
		cryptopiaExchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
		
		accountService = cryptopiaExchange.getAccountService(); // creating services, usage: accountInfo(accountService);
		marketService = cryptopiaExchange.getMarketDataService();
		tradeService = cryptopiaExchange.getTradeService();
		
		this.coinOne = coinOne;
		this.coinTwo = coinTwo;
		currencyPair = new CurrencyPair(coinTwo, coinOne);
		
	}
  
	public static void accountInfo() throws IOException {
		/**
		 * Primary method for account information, such as coin balances.
		 */
		coinOneBalance = accountService.getAccountInfo().getWallet().getBalance(coinOne).getAvailable(); //instead of coinOne used to be Currency.BTC
		System.out.println("coinOneBalance ( " + coinOne.getDisplayName() + " ) = " + coinOneBalance); 
	  
		coinTwoBalance = accountService.getAccountInfo().getWallet().getBalance(coinTwo).getAvailable();
		System.out.println("coinTwoBalance ( " + coinTwo.getDisplayName() + " ) = " + coinTwoBalance); 
		  
	}
  
	public static void tradeInfo() throws IOException {
		/**
		 * Primary method for trading operations, such as placing limit orders (Bid/Ask)
		 */
		//LimitOrder limitOrder = new LimitOrder(OrderType.BID, amountTrading, null, currencyPair, null, null, lowPrice);
		//System.out.println(tradeService.placeLimitOrder(limitOrder));
	  
	}
  
	public static TimeSeries marketInfo(int period) throws IOException {
		/**
		 * Primary method for retrieving market data for a certain period of time.
		 */
		TimeSeries marketData = new TimeSeries(currencyPair.toString());
		//System.setProperty("http.agent", "Chrome");
		try {
			String url = "https://www.cryptopia.co.nz/api/GetMarket/" + coinTwo + "_" + coinOne;
			JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
			
			JSONObject dataObject = (JSONObject) jsonObject.get("Data");
			String tradePairID = dataObject.get("TradePairId").toString();
			
			url = "https://www.cryptopia.co.nz/Exchange/GetTradePairChart?tradePairId=" 
					+ Integer.parseInt(tradePairID) +"&dataRange=7&dataGroup=" + period;
			System.out.println(tradePairID + " " + url);
			JSONParser parser = new JSONParser();
			
			URLConnection conn = new URL(url).openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
			Scanner scn = new Scanner(conn.getInputStream()).useDelimiter("\\A");
			String buffer = scn.hasNext() ? scn.next() : "";
			//Object obj = parser.parse(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
			jsonObject = (JSONObject) parser.parse(buffer);
			//jsonObject = (JSONObject) JSONValue.parseWithException(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
			int x = 0;
			JSONArray arr = (JSONArray) jsonObject.get("Candle");
			for (int i = 0; i < arr.size(); i++)
			{
			    JSONArray ticker = (JSONArray) arr.get(i);
				Date timestamp = new Date((long) ticker.get(0));
				BigDecimal open = new BigDecimal((double) ticker.get(1));
				BigDecimal high = new BigDecimal((double) ticker.get(2));
				BigDecimal low = new BigDecimal((double) ticker.get(3)); 
				BigDecimal close = new BigDecimal((double) ticker.get(4));
				
			    marketData.addOrUpdate(new Millisecond(timestamp), close);
			    System.out.println(x+=1);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return marketData;
	}
  
}
