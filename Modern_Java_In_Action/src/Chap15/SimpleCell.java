package Chap15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Consumer;

// 15.5.1 (두 플로를 합치는 예제 - C1 + C2 = C3, C1,C2의 값이 변경되면 C3에도 새로운 값이 반영)
public class SimpleCell implements Publisher<Integer>, Subscriber<Integer> {

	private int value = 0;
	private String name;
	private List<Subscriber<? super Integer>> subscribers = new ArrayList<>();

	public static void main(String[] args) {
		SimpleCell c2 = new SimpleCell("c2");
		SimpleCell c1 = new SimpleCell("c1");
		SimpleCell c3 = new SimpleCell("c3");

		c1.subscribe(c3);
		c1.onNext(10); // C1의 값을 10으로 갱신
		c2.onNext(20); // C2의 값을 20으로 갱신
	}

	public SimpleCell(String name) {
		this.name = name;
	}

	// c1과 c2에 이벤트가 발행했을때 c3를 구독 → c1, c2의 값이 변경 시, c3가 두값을 더하도록 지정
	@Override
	public void subscribe(Subscriber<? super Integer> subscriber) {
		subscribers.add(subscriber);
	}

	public void subscribe(Consumer<? super Integer> onNext) {
		subscribers.add(new Subscriber<>() {

			@Override
			public void onComplete() {
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			// 정보를 전달할 단순 메서드
			@Override
			public void onNext(Integer val) {
				onNext.accept(val);
			}

			@Override
			public void onSubscribe(Subscription s) {
			}

		});
	}

	private void notifyAllSubscribers() {
		subscribers.forEach(subscriber -> subscriber.onNext(value));
	}

	@Override
	public void onNext(Integer newValue) {
		value = newValue;
		System.out.println(name + ":" + value);
		notifyAllSubscribers();
	}

	@Override
	public void onSubscribe(Subscription s) {
	}

	@Override
	public void onError(Throwable t) {
	}

	@Override
	public void onComplete() {
	}

}
