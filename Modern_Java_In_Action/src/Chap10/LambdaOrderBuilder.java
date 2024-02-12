package Chap10;

import java.util.function.Consumer;

import Chap10.model.Order;
import Chap10.model.Stock;
import Chap10.model.Trade;

// 10.3.3 (람다 표현식을 이용한 함수 시퀀싱)
public class LambdaOrderBuilder {
	public static void main(String[] args) {
		Order order = LambdaOrderBuilder.order(o -> {
			o.forCustomer("BigBank");
			o.buy(t -> {
				t.quantity(80);
				t.price(125.00);
				t.stock(s -> {
					s.symbol("IBM");
					s.market("NYSE");
				});
			});
			o.sell(t -> {
				t.quantity(50);
				t.price(375.00);
				t.stock(s -> {
					s.symbol("GOOGLE");
					s.market("NASDAQ");
				});
			});
		});
	}

	private Order order = new Order();

	public static Order order(Consumer<LambdaOrderBuilder> consumer) {
		LambdaOrderBuilder builder = new LambdaOrderBuilder();
		consumer.accept(builder);
		return builder.order;
	}

	public void forCustomer(String customer) {
		order.setCustomer(customer);
	}

	public void buy(Consumer<TradeBuilder> consumer) {
		trade(consumer, Trade.Type.BUY);
	}

	public void sell(Consumer<TradeBuilder> consumer) {
		trade(consumer, Trade.Type.SELL);
	}

	// TraderBuilder의 Type을 결정하고 람다의 전략대로 TradeBuilder를 수정하고 order에 trade를 더한다.
	// 즉, 일종의 템플릿 메소드 패턴이다.
	private void trade(Consumer<TradeBuilder> consumer, Trade.Type type) {
		TradeBuilder builder = new TradeBuilder();
		builder.trade.setType(type);
		consumer.accept(builder);
		order.addTrade(builder.trade);
	}

	// TradeBuilder Trade 인스턴스를 가지고 있고, 이를 설정할 수 있는 메서드를 가지고 있다.
	public static class TradeBuilder {
		private Trade trade = new Trade();

		public void quantity(int quantity) {
			trade.setQuantity(quantity);
		}

		public void price(double price) {
			trade.setPrice(price);
		}

		// 위 buy, sell 메서드와 비슷하게 StockBuilder를 람다의 전략대로 바꾸고 trade의 stock을 설정한다.
		public void stock(Consumer<StockBuilder> consumer) {
			StockBuilder builder = new StockBuilder();
			consumer.accept(builder);
			trade.setStock(builder.stock);
		}
	}

	public static class StockBuilder {

		private Stock stock = new Stock();

		public void symbol(String symbol) {
			stock.setSymbol(symbol);
		}

		public void market(String market) {
			stock.setMarket(market);
		}

	}
}
