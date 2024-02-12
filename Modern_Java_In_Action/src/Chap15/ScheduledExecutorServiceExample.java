package Chap15;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 15.2.3 (Sleep(또는 blocking)은 해롭다)
public class ScheduledExecutorServiceExample {

	public static void main(String[] args) throws InterruptedException {
		// sleep(blocking) 코드 → 다른 task의 할당을 막음(스레드의 자원 점유) → 밀리면 동작 과부하
//		work1();
//		Thread.sleep(10000); // 10초간 슬립
//		work2();

		// 다른 task의 할당 막지 X → 다른 작업 동시 실행 O
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

		work1();
		scheduledExecutorService.schedule(ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECONDS);

		scheduledExecutorService.shutdown();
	}

	public static void work1() {
		System.out.println("Hello from work1!");
	}

	public static void work2() {
		System.out.println("Hello from work2!");
	}
}
