package Chap17;

import java.util.concurrent.Flow.Publisher;

public class Main {
	public static void main(String[] args) {
		getTemperatures("New York").subscribe(new TempSubscriber());
	}

	private static Publisher<TempInfo> getTemperatures(String town) {
		// 구독한 Subscriber에게 TempSubscription을 전송하는 Publisher를 반환
		return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
	}
}
