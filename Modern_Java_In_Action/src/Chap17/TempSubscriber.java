package Chap17;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class TempSubscriber implements Subscriber<TempInfo> {
	private Subscription subscription;

	@Override
	public void onSubscribe(Subscription subscription) { // 구독을 저장하고 첫 번째 요청을 전달
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(TempInfo tempInfo) { // 수신한 온도를 출력하고 다음 정보를 요청
		System.out.println(tempInfo);
		subscription.request(1);
	}

	@Override
	public void onError(Throwable t) {
		System.out.println(t.getMessage());
	}

	@Override
	public void onComplete() {
		System.out.println("done!");
	}
}