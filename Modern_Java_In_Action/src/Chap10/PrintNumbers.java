package Chap10;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PrintNumbers {
	public static void main(String[] args) {
		// 10.1.2 (JVM에서 이용할 수 있는 다른 DSL 해결책 - 내부 DSL)
		List<String> numbers = Arrays.asList("one", "two", "three");

		// 익명 클래스
		numbers.forEach(new Consumer<String>() {
			@Override
			public void accept(String s) {
				System.out.println(s);
			}
		});

		// 람다 표현식(즉, 내부 DSL)
		numbers.forEach(s -> System.out.println(s));

		// 메소드 참조
		numbers.forEach(System.out::println);
	}

}
