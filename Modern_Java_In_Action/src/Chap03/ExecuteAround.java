package Chap03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecuteAround {

	private static final String FILE = ExecuteAround.class.getResource("./test.txt").getFile();

	public static void main(String[] args) throws IOException {
		// Runnable 인터페이스와 같은 시그니처를 갖는 람다식이 온다.
		process(() -> System.out.println("함수형 인터페이스"));

		// 3.3.1(두줄씩 읽으려면? 두번씩 동작: 동작 파라미터화)
//		String result = processFile((BufferedReader br) -> br.readLine() + br.readLine());

		// 3.3.4(람다 전달)
		String oneLine = processFile((BufferedReader br) -> br.readLine());
		System.out.println(oneLine);

		String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
		System.out.println(twoLine);

		// 3.4.1
		List<String> strings = Arrays.asList("모던", "", "자바", "인", "", "액션");
		System.out.println(strings);
		Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
		List<String> nonEmpty = filter(strings, nonEmptyStringPredicate);
		System.out.println(nonEmpty);

		// 3.4.2
		forEach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.println(i));

		// 3.4.3
		List<Integer> integerList = map(Arrays.asList("모던", "", "자바", "인", "", "액션"), (String s) -> s.length());
		System.out.println(integerList);

		// 3.4.4
		// 박싱 되지 않음
		IntPredicate evenNumbers = (int i) -> i % 2 == 0;
		evenNumbers.test(1000); // true

		// int형 값이 Integer타입으로 오토박싱 됨
		Predicate<Integer> oddNumbers = (Integer i) -> i % 2 != 0;
		oddNumbers.test(1000); // false

		// 3.4.5 (3.3.4의 BufferedReaderProcessor인터페이스를 이용한 직접 함수형 인터페이스로 예외를 잡기 O
		// - Funtion<T,R>)
		Funtion<BufferedReader, String> f = (BufferedReader br) -> {
			try {
				return br.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};

		// 3.5.2
		// 다이아몬드 연산자 - 타입을 추론(뒤에 <>을 비우지만 컴파일러가 알아서 제네릭 형식 추론)
		List<String> strings1 = new ArrayList<>();
		List<Integer> integers1 = new ArrayList<>();

		// 특별한 void 호환 규칙 - list.add(s)는 boolean 값을 반환 but,Consumer와도 호환 O → void를 반환하는
		// 시그니처의 경우 다른 타입도 받을 수 있다.
		// Predicate는 불리언 반환값을 갖는다.
		List<String> list = new ArrayList<>();
		Predicate<String> p = s -> list.add(s);
		p.test("a");
		System.out.println(list.toString());
		// Consumer는 void 반환값을 갖는다.
		Consumer<String> b = s -> list.add(s);
		b.accept("b");
		System.out.println(list.toString());

	}

	// 3.2.2
	// Runnable의 run 메서드는 인수와 반환값 X → Runnable은 인수와 반환값이 없는 시그니처
	public static void process(Runnable r) {
		r.run();
	}

	// 3.3 (실행 어라운드 패턴)
	// 실행 어라운드 패턴: 실행 전 준비와 마무리 작업이 있고, 그 사이에 어떤 자원을 처리하는 작업이 있는 패턴
	// → 자원 준비,정리 코드 변함 X → 동작 파라미터화 적용 O
	public static String processFileLimited() throws IOException {
		// try-with-resources: try 문이 끝나면 자원도 반납한다.
		try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
			// 실제 작업을 수행(한줄씩 읽음)
			return br.readLine();
		}
	}

	// 3.3.2 (함수형 인터페이스)
	@FunctionalInterface // 함수형 인터페이스 표시(추상 메서드는 오직 하나, default method, static method 가능)
	public interface BufferedReaderProcessor {
		// 단일 추상 메서드 process를 선언 위(process)와 다름
		String process(BufferedReader b) throws IOException;

		// default method 는 존재해도 상관없음
		default void printDefault() {
			System.out.println("Hello Default");
		}

		// static method 는 존재해도 상관없음
		static void printStatic() {
			System.out.println("Hello Static");
		}
	}

	// 3.3.3 (동작)
	// 위 함수형 인터페이스(BufferedReaderProcessor)를 processFile메서드의 인수(=파라미터)로 전달
	public static String processFile(BufferedReaderProcessor p) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
			// p(BufferedReaderProcessor)의 process에 br(BufferedReader)을 받아 처리
			return p.process(br);
		}
	}

	// 3.4.1 (Predicate)
	@FunctionalInterface
	// Predicate: test(T의 객체(t)를 인수로 받음)라는 추상 메서드를 정의
	// boolean 표현식이 필요한 상황에 사용
	public interface Predicate<T> {
		boolean test(T t);
	}

	public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<>();
		for (T e : list) {
			if (p.test(e)) {
				result.add(e);
			}
		}
		return result;
	}

	// 3.4.2 (Consumer)
	@FunctionalInterface
	// Consumer:제너릭 형식 T 객체(t)를 받아서 void(accept(추상 메서드))를 반환
	// 어떤 동작을 수행하고싶을 때 사용
	public interface Consumer<T> {
		void accept(T t);
	}

	public static <T> void forEach(List<T> list, Consumer<T> c) {
		for (T t : list) {
			c.accept(t);
		}
	}

	// 3.4.3 (Function<T,R>)
	@FunctionalInterface
	// Function:제너릭 형식 T를 인수로 받아 제네릭 형식 R 객체(t)를 반환하는 추상 메서드(apply)
	// 입력을 출력으로 매핑하는 람다를 정의할 때 사용
	public interface Funtion<T, R> {
		R apply(T t);
	}

	public static <T, R> List<R> map(List<T> list, Funtion<T, R> f) {
		List<R> result = new ArrayList<>();
		for (T t : list) {
			result.add(f.apply(t));
		}
		return result;
	}

	// 3.4.4 (기본형 특화)
	// 오토박싱 동작을 피하는 특별한 버전 함수형 인터페이스
	@FunctionalInterface
	public interface IntPredicate {
		boolean test(int t);
	}
}