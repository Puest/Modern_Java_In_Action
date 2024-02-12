package Chap16;

import java.util.Random;

// 16.2(비동기 API 구현)
// 동기 API
public class SyncShop {
	private final String name;
	private final Random random;

	public SyncShop(String name) {
		this.name = name;
		random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
	}

	public double getPrice(String product) {
		return 0;
	}

	private double calculatePrice(String product) {
		delay(); // 1초간 블록(sleep)
		return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
	}

	public static void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
