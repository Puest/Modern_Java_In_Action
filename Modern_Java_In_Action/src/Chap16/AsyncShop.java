package Chap16;

import static Chap16.Util.delay;
import static Chap16.Util.format;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import modernjavainaction.chap16.Discount;

// 16.2.1 (동기 메서드 → 비동기 메서드)
public class AsyncShop {
	private final String name;
	private final Random random;

	public AsyncShop(String name) {
		this.name = name;
		random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
	}

	public Future<Double> getPriceAsync(String product) {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();

		new Thread(() -> {
			try {
				double price = calculatePrice(product); // 다른 스레드에서 비동기로 계산
				futurePrice.complete(price); // 결과값 전달
			} catch (Exception ex) {
				futurePrice.completeExceptionally(ex); // 문제가 발생하는 경우 에러를 포함시켜 Future를 종료
			}
		}).start();

		// 팩토리 메서드 supplyAsync로 CompletableFuture 만들기
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}

	public String getPrice(String product) {
		double price = calculatePrice(product);
		Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
		return name + ":" + price + ":" + code;
	}

	private double calculatePrice(String product) {
		delay();
		if (true) {
			throw new RuntimeException("product not available");
		}

		return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
	}

	public String getName() {
		return name;
	}
}
