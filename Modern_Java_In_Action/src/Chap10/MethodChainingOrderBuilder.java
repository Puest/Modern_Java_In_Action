package Chap10;

import Chap10.model.Order;
import Chap10.model.Stock;
import Chap10.model.Trade;

// 10.3.1 (메서드 체인 - 주문 객체를 포함하고 메소드 체인 DSL을 제공하는 주문 빌더)
public class MethodChainingOrderBuilder {
	public static void main(String[] args) {
		// 이용(메소드 체인)
		Order order = forCustomer("BigBank").buy(80).stock("IBM").on("NYSE").at(125.00).sell(50).stock("GOOGLE")
				.on("NASDAQ").at(375.00).end();
		System.out.println(order);
	}

	public final Order order = new Order();

	private MethodChainingOrderBuilder(String customer) {
		order.setCustomer(customer);
	}

	public static MethodChainingOrderBuilder forCustomer(String customer) {
		return new MethodChainingOrderBuilder(customer);
	}

	// 주문 만들기를 종료하고 반환
	public Order end() {
		return order;
	}

	// 주식을 사는 트레이더 빌더를 만든다.
	public TradeBuilder buy(int quantity) {
		return new TradeBuilder(this, Trade.Type.BUY, quantity);
	}

	// 주식을 파는 트레이더 빌더를 만든다.
	public TradeBuilder sell(int quantity) {
		return new TradeBuilder(this, Trade.Type.SELL, quantity);
	}

	// 주문에 주식을 추가
	private MethodChainingOrderBuilder addTrade(Trade trade) {
		order.addTrade(trade);
		return this;
	}

	// 트레이드 빌더
	// 주문을 가지고 StcokBuilder를 생성한다.
	public static class TradeBuilder {

		private final MethodChainingOrderBuilder builder;
		public final Trade trade = new Trade();

		private TradeBuilder(MethodChainingOrderBuilder builder, Trade.Type type, int quantity) {
			this.builder = builder;
			trade.setType(type);
			trade.setQuantity(quantity);
		}

		public StockBuilder stock(String symbol) {
			return new StockBuilder(builder, trade, symbol);
		}
	}

	// 스톡 빌더
	// 주식의 시장을 지정하고, 거래에 주식을 추가하고, 최종 빌더를 반환하는 on메서드 정의
	public static class StockBuilder {

		private final MethodChainingOrderBuilder builder;
		private final Trade trade;
		private final Stock stock = new Stock();

		private StockBuilder(MethodChainingOrderBuilder builder, Trade trade, String symbol) {
			this.builder = builder;
			this.trade = trade;
			stock.setSymbol(symbol);
		}

		public TradeBuilderWithStock on(String market) {
			stock.setMarket(market);
			trade.setStock(stock);
			return new TradeBuilderWithStock(builder, trade);
		}
	}

	// 공개 메서드 TradeBuilderWithStock은 거래되는 주식의 단위 가격을 설정한 다음 원래 주문 빌더를 반환한다.
	public static class TradeBuilderWithStock {

		private final MethodChainingOrderBuilder builder;
		private final Trade trade;

		public TradeBuilderWithStock(MethodChainingOrderBuilder builder, Trade trade) {
			this.builder = builder;
			this.trade = trade;
		}

		public MethodChainingOrderBuilder at(double price) {
			trade.setPrice(price);
			return builder.addTrade(trade);
		}
	}

}
