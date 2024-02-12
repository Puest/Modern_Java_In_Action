package Chap16;

import java.util.concurrent.Future;

public class AsynShopMain {

	public static void main(String[] args) {
		AsyncShop shop = new AsyncShop("BestShop");
		long start = System.nanoTime();
		Future<Double> futurePrice = shop.getPriceAsync("my favorite product"); // 제품 가격 요청
		long invocationTime = ((System.nanoTime() - start) / 1_000_000);

		// 다른 상점 질의 같은 다른 작업 수행
		doSomethingElse();
		// 제품 가격을 계산하는 동안
		try {
			double price = futurePrice.get(); // 가격 정보를 받을때까지 블록
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
	}

	private static void doSomethingElse() {
		System.out.println("Doing something else...");
	}

}
