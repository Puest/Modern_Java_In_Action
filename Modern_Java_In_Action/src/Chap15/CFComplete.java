package Chap15;

import static Chap15.Functions.f;
import static Chap15.Functions.g;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 15.4 (CompletableFuture와 콤비네이터를 이용한 동시성)
public class CFComplete {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		int x = 1337;

		// CompletableFuture - Future를 조합 → get으로 결과 얻음
		CompletableFuture<Integer> a = new CompletableFuture<>();
		executorService.submit(() -> a.complete(f(x)));
		int b = g(x);
		System.out.println(a.get() + b);

		executorService.shutdown();
	}
}
