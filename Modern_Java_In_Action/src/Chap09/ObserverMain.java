package Chap09;

import java.util.ArrayList;
import java.util.List;

// 9.2.3 (옵저버)
public class ObserverMain {
	public static void main(String[] args) {
		Feed f = new Feed();
		f.registerObserver(new NYTimes());
		f.registerObserver(new Guardian());
		f.registerObserver(new LeMonde());
		f.notifyObservers("The queen said her favourite book is Java 8 & 9 in Action!");

		// 람다
		// Observer가 함수형 인터페이스이므로 람다를 통해 옵저버의 notify가 불리면 행해질 일을 전달 O
		// Observer notify 시그니처에 맞게 전달
		Feed feedLambda = new Feed();

		feedLambda.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("money")) {
				System.out.println("Breaking news in NY! " + tweet);
			}
		});
		feedLambda.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("queen")) {
				System.out.println("Yet another news in London... " + tweet);
			}
		});
		feedLambda.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("wine")) {
				System.out.println("Today cheese, wine and news! " + tweet);
			}
		});

		feedLambda.notifyObservers("Money money money, give me money!");
	}

	// 알림을 받을 notify 메서드를 가져야한다.
	interface Observer {
		void notify(String tweet);
	}

	// 여러 옵저버들을 구현 O
	// 주체 객체가 notify하면 옵저버들의 notify 함수가 불려 알림을 준다.
	static private class NYTimes implements Observer {
		@Override
		public void notify(String tweet) {
			if (tweet != null && tweet.contains("money")) {
				System.out.println("Breaking news in NY! " + tweet);
			}
		}
	}

	static private class Guardian implements Observer {
		@Override
		public void notify(String tweet) {
			if (tweet != null && tweet.contains("queen")) {
				System.out.println("Yet another news in London... " + tweet);
			}
		}
	}

	static private class LeMonde implements Observer {
		@Override
		public void notify(String tweet) {
			if (tweet != null && tweet.contains("wine")) {
				System.out.println("Today cheese, wine and news! " + tweet);
			}
		}
	}

	// 구성하고 있는 옵저버 리스트에 옵저버를 등록하고 옵저버에 notify 할 수 있는 메서드를 가져야 함
	interface Subject {
		void registerObserver(Observer o);

		void notifyObservers(String tweet);
	}

	static private class Feed implements Subject {
		private final List<Observer> observers = new ArrayList<>();

		@Override
		public void registerObserver(Observer o) {
			this.observers.add(o);
		}

		@Override
		public void notifyObservers(String tweet) {
			observers.forEach(o -> o.notify(tweet));
		}

	}
}
