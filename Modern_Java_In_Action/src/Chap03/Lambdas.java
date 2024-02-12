package Chap03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import Chap03.ExecuteAround.Funtion;

public class Lambdas {
	public static void main(String[] args) {
		List<Apple> inventory = Arrays.asList(new Apple(80, Color.GREEN), new Apple(155, Color.GREEN),
				new Apple(120, Color.RED));

		List<Apple> heavyApples = filter(inventory, a -> a.getWeight() > 150);
		System.out.println(heavyApples);

		// 3.5.3 (형식 추론)
		// o1과 o2의 형식을 추론함 (가독성 향상)
//		Comparator<Apple> = (o1, o2) -> Integer.compare(o1.getWeight(), o2.getWeight());

		// o1과 o2의 형식을 추론하지 않음(Apple o1, Apple o2 지정)
//		Comparator<Apple> = (Apple o1, Apple o2) -> Integer.compare(o1.getWeight(), o2.getWeight());

		// 3.5.4
		// 지역 변수 사용 = 람다 캡처링
		int portNumber = 1080; // 지역 변수 = 자유변수(final로 선언 되거나 final로 선언된 변수같이 사용)
		Runnable r = () -> System.out.println(portNumber);
		r.run();

		// Error(final을 설정한 변수처럼 사용하기때문에 값이 변경되면 오류 발생)
//		portNumber = 31337;

		// 클로저 - 람다와 비슷
		// (차이점:외부에 정의된 변수 값에 접근+값 변경 O but, 람다는 final 변수(final처럼 쓰이는 변수)에만 접근 O+값 변경 X)
		int a = 3; // 자유 변수
		InterfaceRamda interfaceRamda = i -> i + a; // 클로저
		System.out.println(interfaceRamda.test(11));

		// 3.6 (메서드 참조)
//		Comparator<Apple> c = (Apple o1, Apple o2) -> o1.getWeight() - o2.getWeight();
//		inventory.sort(c);
		inventory.sort(Comparator.comparing(Apple::getWeight)); // 메서드 참조(람다의 축약형, 가독성 ↑)

		// 3.6.1
		// 메서드 참조(비공개 헬퍼 메서드)
//		filter(inventory, this::isVaildName);

		// List에 포함된 문자열을 대소문자를 구분하지 않고 정렬하는 프로그램
		// sort 메서드는 인자로 Comparator 인터페이스를 갖는데, 이는 (T,T) -> int 함수 디스크립터를 갖는다.
		List<String> str = Arrays.asList("A", "b", "a", "B");
//		Comparator<String> c = (s1, s2) -> s1.compareToIgnoreCase(s2);
//		str.sort(c);
		str.sort(String::compareToIgnoreCase);
		System.out.println(str);

		// 3.6.2 (생성자 참조)
		// 1. 인수가 없는 생성자
		// 람다 표현식 - Supplier<T>는 ()->T 와 같은 메서드 시그니처를 갖는다.
//		Supplier<Apple> c1 = () -> new Apple();
//		Apple apple = c1.get();

		// 메서드 참조
		Supplier<Apple> c1 = Apple::new;
		Apple apple = c1.get();

		// 2.Apple(Integer weight) 시그니처를 갖는 생성자
		// 람다 표현식 - Function<T, R>은 (T) -> R 과 같은 메서드 시그니처를 갖는다.
		// 즉, Integer를 인수로 받아 새로운 객체를 생성하는 new Apple(Integer weight)와 시그니처가 일치한다.
//		Funtion<Integer, Apple> c2 = (weight) -> new Apple(weight);
//		Apple apple2 = c2.apply(100);

		// 메서드 참조
		Funtion<Integer, Apple> c2 = Apple::new;
		Apple apple2 = c2.apply(100);

		// 다양한 무게를 포함하는 사과 리스트
		List<Integer> weights = Arrays.asList(1, 2, 3, 4);
		List<Apple> apples = map(weights, Apple::new);
		System.out.println(apples);

		// 3. Apple(Integer weight, String color) 시그니처를 갖는 생성자
		// 람다 표현식 - BiFunction<T, U, R>은 (T,U) -> R 과 같은 메서드 시그니처를 갖는다.
		// 즉 Integer와 Color를 인수로 받아 새로운 객체를 생성하는 new Apple(String color, Integer
		// weight)와 시그니처가 일치한다.
//		BiFunction<Integer, Color, Apple> c3 = (integer, color) -> new Apple(integer, color);
//		Apple apple3 = c3.apply(100, Color.RED);

		// 메서드 참조
		BiFunction<Integer, Color, Apple> c3 = Apple::new;
		Apple apple3 = c3.apply(100, Color.RED);
		System.out.println(apple3);

		System.out.println(getFruit("apple", 100));
	}

	// 외부 정의된 변수 값(클로저)
	@FunctionalInterface
	interface InterfaceRamda {
		int test(int n);
	}

	// 3.5.1 (형식 검사)
	public static List<Apple> filter(List<Apple> inventory, ApplePredicate p) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}

	// Predicate
	// 함수형 인터페이스(추상 메서드가 Only 하나)
	interface ApplePredicate {
		// 추상메서드
		boolean test(Apple a);
	}

	// 3.6.1
	// 비공개 헬퍼 메서드
	private boolean isVaildName(String string) {
		return Character.isUpperCase(string.charAt(0));
	}

	// 3.6.2
	// 다양한 무게를 포함하는 사과 리스트
	public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<>();
		for (T t : list) {
			result.add(f.apply(t));
		}
		return result;
	}

	// 3.6.2
	// 4. 인스턴스화 하지않고도 생성자에 접근하는 기능
	// 생성하는 기능들만 구현
	static Map<String, Function<Integer, Apple>> map = new HashMap<>();
	static {
		map.put("apple", Apple::new);
	}

	// apply 메서드에 정수 파라미터를 제공해서 실제 인스턴스 생성
	public static Apple getFruit(String fruit, Integer weight) {
		return map.get(fruit.toLowerCase()).apply(weight);
	}
}
