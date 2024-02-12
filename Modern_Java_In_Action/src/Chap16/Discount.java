package Chap16;

import static Chap16.Util.delay;
import static Chap16.Util.format;

// 16.4 (비동기 작업 파이프라인 만들기)
public class Discount {
	// 상점이 하나의 할인 서비스를 사용한다고 가정(enum으로 할인율을 제공하는 코드를 정의)
	public enum Code {
		NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMONE(20);

		private final int percentage;

		Code(int percentage) {
			this.percentage = percentage;
		}
	}

	// 16.4.1 메서드(Quote 객체를 인수로 받음 → 할인된 가격 반환)
	public static String applyDiscount(Quote quote) {
		return quote.getShopName() + "price is " + Discount.apply(quote.getprice(), quote.getDiscountCode());
	}

	private static double apply(double price, Code code) {
		delay(); // 원격 서비스이므로 1초 지연 추가
		return format(price * (100 - code.percentage) / 100);
	}
}
