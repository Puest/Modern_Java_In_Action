package Chap15;

import static Chap15.Functions.f;
import static Chap15.Functions.g;

// 15.2 동기 API(Runnalbe)와 비동기 API
public class ThreadExample {
	// Runnable - 서로 상호작용 X, 별도의 CPU코어로 실행 → 시간 단축 + 코드 복잡성 ↑
	public static void main(String[] args) throws InterruptedException {
		int x = 1337;
		Result result = new Result();

		Thread t1 = new Thread(() -> {
			result.left = f(x); // x * 2
		});
		Thread t2 = new Thread(() -> {
			result.right = g(x); // x + 1
		});

		t1.start();
		t2.start();
		t1.join();
		t2.join();

		System.out.println(result.left + result.right); // 2674 + 1338 → 4012
	}

	private static class Result {
		private int left;
		private int right;
	}
}
