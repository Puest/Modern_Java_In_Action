package Chap10;

import java.util.function.Consumer;
import java.util.stream.Stream;

import Chap10.model.Order;
import Chap10.model.Stock;
import Chap10.model.Trade;

// 10.3.4 (조합하기 - 중첩된 함수 패턴과 람다 기법의 혼용)
// ※ 여러 장점이 모두 들어있음 but, 사용자가 DSL을 배우기까지 시간이 오래 걸림
public class MixedBuilder {
	public static void main(String[] args) {
		Order order = forCustomer("BigBank", buy(t -> t.quantity(80).stock("IBM").on("NYSE").at(125.00)),
				sell(t -> t.quantity(50).stock("GOOGLE").on("NASDAQ").at(375.00)));
		System.out.println(order);
	}

	// 중첩된 함수 패턴
	public static Order forCustomer(String customer, TradeBuilder... builders) {
		Order order = new Order();
		order.setCustomer(customer);
		Stream.of(builders).forEach(b -> order.addTrade(b.trade));
		return order;
	}

	// 람다
	public static TradeBuilder buy(Consumer<TradeBuilder> consumer) {
		return buildTrade(consumer, Trade.Type.BUY);
	}

	public static TradeBuilder sell(Consumer<TradeBuilder> consumer) {
		return buildTrade(consumer, Trade.Type.SELL);
	}

	private static TradeBuilder buildTrade(Consumer<TradeBuilder> consumer, Trade.Type buy) {
		TradeBuilder builder = new TradeBuilder();
		builder.trade.setType(buy);
		consumer.accept(builder);
		return builder;
	}

	// 메소드 체인 패턴 사용
	public static class TradeBuilder {
		private Trade trade = new Trade();

		public TradeBuilder quantity(int quantity) {
			trade.setQuantity(quantity);
			return this;
		}

		public TradeBuilder at(double price) {
			trade.setPrice(price);
			return this;
		}

		public StockBuilder stock(String symbol) {
			return new StockBuilder(this, trade, symbol);
		}
	}

	public static class StockBuilder {
		private final TradeBuilder builder;
		private final Trade trade;
		private final Stock stock = new Stock();

		private StockBuilder(TradeBuilder builder, Trade trade, String symbol) {
			this.builder = builder;
			this.trade = trade;
			stock.setSymbol(symbol);
		}

		public TradeBuilder on(String market) {
			stock.setMarket(market);
			trade.setStock(stock);
			return builder;
		}
	}
}
