package Chap15;

import static modernjavainaction.chap15.Functions.f;
import static modernjavainaction.chap15.Functions.g;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//CompletableFuture의 thenCombine을 활용 → 다른 Function을 얻음 → 연산결과 효과적으로 더함 
public class CFCombine {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		int x = 1337;

		CompletableFuture<Integer> a = new CompletableFuture<>();
		CompletableFuture<Integer> b = new CompletableFuture<>();

		CompletableFuture<Integer> c = a.thenCombine(b, (y, z) -> y + z);
		executorService.submit(() -> a.complete(f(x)));
		executorService.submit(() -> b.complete(g(x)));

		System.out.println(c.get());
		executorService.shutdown();
	}
}
