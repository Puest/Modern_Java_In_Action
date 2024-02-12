package Chap03;

import static java.util.Comparator.comparing;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Sorting {
	public static void main(String[] args) {
		List<Apple> inventory = Arrays.asList(new Apple(80, Color.GREEN), new Apple(155, Color.GREEN),
				new Apple(120, Color.RED), new Apple(120, Color.GREEN));

		// 3.7.1
//		inventory.sort(new AppleComparator());

		// 3.7.2 - 2단계(익명 클래스 사용)
		inventory.sort(new Comparator<Apple>() {
			@Override
			public int compare(Apple a1, Apple a2) {
				return a1.getWeight() - a2.getWeight();
			}
		});

		// 3.7.3 - 3단계(람다 사용)
		inventory.sort((Apple a1, Apple a2) -> a1.getWeight() - a2.getWeight());

		// 자바 컴파일러는 형식을 추론한다.
		// 즉, 컴파일러가 inventory 내부 원소의 자료형이 Apple임을 알게되어 a1과 a2의 형식을 지정하지 않아도 됨.
		inventory.sort((a1, a2) -> a1.getWeight() - a2.getWeight());

		// comparing 메소드는 비교하는데 사용될 값을 Function<T,R> 인터페이스로 받아서 Comparator를 반환
		Comparator<Apple> c = Comparator.comparing((Apple a) -> a.getWeight());

		// c 변수를 생성해 넣어도 되지만 아래처럼 한줄로 간소화한다.
		inventory.sort(comparing(apple -> apple.getWeight()));

		// 3.7.4 - 4단계(메서드 참조 사용)
		inventory.sort(comparing(Apple::getWeight));

		// 3.8.1 (Comparator 조합)
		// 1. 역정렬 - reversed()를 사용해 비교자의 순서를 뒤바꿈
		inventory.sort(comparing(Apple::getWeight).reversed());

		// 2. Comparator 연결 - thenComparing를 사용해 두번째 비교자 제작
		inventory.sort(comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
		System.out.println(inventory);

		// 3.8.2 (Predicate 조합)
		// 복잡한 Predicate를 만들 수 있게 negate, and, or 메서드를 제공
		Predicate<Apple> redApple = (a) -> a.getColor() == Color.RED;

		// 빨간 사과가 아닌 사과 - Predicate 결과에 not을 씌운다.
		redApple.negate();

		// 빨간 사과이면서 150그램 이상인 사과
		redApple.and(apple -> apple.getWeight() > 150);

		// 빨간 사과이면서 150그램 이상인 사과 이거나 초록 사과
		redApple.and(apple -> apple.getWeight() > 150).or(a -> a.getColor() == Color.GREEN);

		// 3.8.3 (Function 조합)
		// Function - Function 인스턴스를 반환하는 디폴트 메서드를 제공
		// andThen - 주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전달하는 함수 반환
		Function<Integer, Integer> f = x -> x + 1;
		Function<Integer, Integer> g = x -> x * 2;
		Function<Integer, Integer> h = f.andThen(g); // g(f(x))
		// 결과는 4
		h.apply(1);

		// compose - 인수로 주어진 함수를 먼저 실행한 다음에 그 결과를 외부 함수의 인수로 제공
		Function<Integer, Integer> z = x -> x + 1;
		Function<Integer, Integer> q = x -> x * 2;
		Function<Integer, Integer> y = f.andThen(g); // z(q(x))
		// 결과는 3
		h.apply(1);

	}

	// 3.7.1 - 1단계(sort 메서드)
	// 두사과를 비교
	static class AppleComparator implements Comparator<Apple> {
		@Override
		public int compare(Apple a1, Apple a2) {
			return a1.getWeight() - a2.getWeight();
		}
	}

}
