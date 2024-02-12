package Chap15;

import static Chap15.Functions.f;
import static Chap15.Functions.g;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// 15.2 동기 API와 비동기 API(Future API)
public class ExecutorServiceExample {
	// FutureAPI 인터페이스 활용 (ExecutorService로 스레드 풀을 설정했다는 가정 하) → 명시적인 메서드(submit)
	// 호출(불필요한 코드) → 코드 오염
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int x = 1337;

		ExecutorService executorService = Executors.newFixedThreadPool(2);

		Future<Integer> y = executorService.submit(() -> f(x));
		Future<Integer> z = executorService.submit(() -> g(x));

		System.out.println(y.get() + z.get());

		executorService.shutdown();
	}

}
