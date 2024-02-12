package Chap19;

import java.util.function.Predicate;
import java.util.function.Supplier;

// 19.3.2 (게으른 리스트 만들기)
public class LazyLists {
	public static void main(String[] args) {
		MyList<Integer> I = new MyLinkedList<>(5, new MyLinkedList<>(10, new Empty()));
		System.out.println(I.head());

		LazyList<Integer> numbers = from(2);
		int two = numbers.head();
		int three = numbers.tail().head();
		int four = numbers.tail().tail().head();
		System.out.println(two + " " + three + " " + four);

	}

	// 기본적인 연결 리스트
	interface MyList<T> {
		T head();

		MyList<T> tail();

		default boolean isEmpty() {
			return true;
		}

		MyList<T> filter(Predicate<T> p);
	}

	static class MyLinkedList<T> implements MyList<T> {
		private final T head;
		private final MyList<T> tail;

		public MyLinkedList(T head, MyList<T> tail) {
			this.head = head;
			this.tail = tail;
		}

		@Override
		public T head() {
			return head;
		}

		@Override
		public MyList<T> tail() {
			return tail;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public MyList<T> filter(Predicate<T> p) {
			return isEmpty() ? this : p.test(head()) ? new MyLinkedList<>(head(), tail().filter(p)) : tail().filter(p);
		}
	}

	static class Empty<T> implements MyList<T> {
		@Override
		public T head() {
			throw new UnsupportedOperationException();
		}

		@Override
		public MyList<T> tail() {
			throw new UnsupportedOperationException();
		}

		@Override
		public MyList<T> filter(Predicate<T> p) {
			return isEmpty() ? this : p.test(head()) ? new MyLinkedList<>(head(), tail().filter(p)) : tail().filter(p);
		}
	}

	// 기본적인 게으른 리스트
	static class LazyList<T> implements MyList<T> {
		final T head;
		final Supplier<MyList<T>> tail;

		public LazyList(T head, Supplier<MyList<T>> tail) {
			this.head = head;
			this.tail = tail;
		}

		@Override
		public T head() {
			return head;
		}

		@Override
		public MyList<T> tail() {
			return tail.get();
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		// 게으른 필터 구현
		@Override
		public MyList<T> filter(Predicate<T> p) {
			return isEmpty() ? this
					: p.test(head()) ? new LazyList<T>(head(), () -> tail().filter(p)) : tail().filter(p);
		}
	}

	// LazyList 생성자에 tail인수로 Supplier를 전달 → n으로 시작하는 무한히 게으르 리스트 제작
	public static LazyList<Integer> from(int n) {
		return new LazyList<Integer>(n, () -> from(n + 1));
	}

	// 재귀적으로 호출
	static <T> void printAll(MyList<T> list) {
		if (list.isEmpty()) {
			return;
		}
		System.out.println(list.head());
		printAll(list.tail());
	}
}
