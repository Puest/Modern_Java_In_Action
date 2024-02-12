package Chap10;

import java.util.stream.Stream;

import Chap10.model.Order;
import Chap10.model.Stock;
import Chap10.model.Trade;

// 10.3.2 (중첩된 함수 이용 - 다른 함수 안에 함수를 이용해 도메인 모델 만듦)
public class NestedFunctionOrderBuilder {

	public static void main(String[] args) {
		Order order = Order("BigBank", buy(80, stock("IBM", on("NYSE")), at(125.00)),
				sell(50, stock("GOOGLE", on("NASDAQ")), at(375.00)));
		System.out.println(order);
	}

	private static Order Order(String customer, Trade... trades) {
		Order order = new Order();
		order.setCustomer(customer);
		Stream.of(trades).forEach(order::addTrade);
		return order;
	}

	public static Trade buy(int quantity, Stock stock, double price) {
		return buildTrade(quantity, stock, price, Trade.Type.BUY);
	}

	public static Trade sell(int quantity, Stock stock, double price) {
		return buildTrade(quantity, stock, price, Trade.Type.SELL);
	}

	private static Trade buildTrade(int quantity, Stock stock, double price, Trade.Type buy) {
		Trade trade = new Trade();
		trade.setQuantity(quantity);
		trade.setType(buy);
		trade.setStock(stock);
		trade.setPrice(price);
		return trade;
	}

	public static double at(double price) {
		return price;
	}

	public static Stock stock(String symbol, String market) {
		Stock stock = new Stock();
		stock.setSymbol(symbol);
		stock.setMarket(market);
		return stock;
	}

	public static String on(String market) {
		return market;
	}

}
