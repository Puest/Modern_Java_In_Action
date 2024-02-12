package Chap10.model;

// 10.3 (주식 가격을 모델링 - 순수 자바 Beans)
public class Stock {
	private String symbol;
	private String market;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	@Override
	public String toString() {
		return String.format("Stock[symbol=%s, market=%s]", symbol, market);
	}
}
