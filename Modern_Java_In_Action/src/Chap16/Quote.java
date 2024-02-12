package Chap16;

// 16.4.1 (할인 서비스 구현 - 할인 정보가 언제든 변경될 수 있으므로 매번 서버에서 받아오는 걸로 가정)
// 상점(DiscntShop)에서 제공한 문자열 파싱 → Quote 클래스로 캡슐화
public class Quote {
	private final String shopName;
	private final double price;
	private final Discount.Code discountCode;

	public Quote(String shopName, double price, Discount.Code discountCode) {
		this.shopName = shopName;
		this.price = price;
		this.discountCode = discountCode;
	}

	// 정적 팩토리 메서드(상점 Str → parse → Quote 클래스 인스턴스 생성)
	public static Quote parse(String s) {
		String[] split = s.split(":");
		String shopName = split[0];
		double price = Double.parseDouble(split[1]);
		Discount.Code discountCode = Discount.Code.valueOf(split[2]);
		return new Quote(shopName, price, discountCode);
	}

	public String getShopName() {
		return shopName;
	}

	public double getprice() {
		return price;
	}

	public Discount.Code getDiscountCode() {
		return discountCode;
	}
}
