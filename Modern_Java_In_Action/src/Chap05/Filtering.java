package Chap05;

import static Chap04.Dish.menu;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import Chap04.Dish;

public class Filtering {
	public static void main(String[] args) {
		// 5
		// 컬렉션 반복(→ 외부 반복)
		List<Dish> vegetarianDishes = new ArrayList<>();
		for (Dish d : menu) {
			if (d.isVegetarian()) {
				vegetarianDishes.add(d);
			}
		}

		// 스트림 API 사용 (→ 내부 반복으로 변경)
		List<Dish> vegetarianDishes2 = menu.stream().filter(Dish::isVegetarian).collect(toList());

		// 5.1.2 (고유 요소 필터링 - distinct)
		List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
		numbers.stream().filter(i -> i % 2 == 0).distinct() // 중복 요소 제거
				.forEach(System.out::println);

		// 5.2 (스트림 슬라이싱)
		List<Dish> specialMenu = Arrays.asList(new Dish("season fruit", true, 120, Dish.Type.OTHER),
				new Dish("prawns", false, 300, Dish.Type.FISH), new Dish("rice", true, 350, Dish.Type.OTHER),
				new Dish("chicken", false, 400, Dish.Type.MEAT), new Dish("french fries", true, 530, Dish.Type.OTHER));

		// 5.2.1
		// takeWhile - 전체 스트림 반복(조건 X) → 중단 ,filter:전체 스트림 반복 → 요소 만
		List<Dish> slicedMenu1 = specialMenu.stream().takeWhile(dish -> dish.getCalories() < 320) // 320 칼로리 이하의 요리들
				.collect(toList());

		// dropWhile - 프레디케이트가 거짓이 되면 그 지점에서 작업을 중단하고 남은 모든 요소를 반환
		List<Dish> slicedMenu2 = specialMenu.stream().dropWhile(dish -> dish.getCalories() < 320).collect(toList());

		// 5.2.2 (스트림 축소 - limit: 주어진 값 이하의 값을 반환)
		List<Dish> dishes = specialMenu.stream().filter(dish -> dish.getCalories() > 300).limit(3).collect(toList());

		// 5.2.3 (요소 건너뛰기 - skip: 처음 n개 요소를 제외한 스트림 반환)
		List<Dish> dishes2 = specialMenu.stream().filter(dish -> dish.getCalories() > 300).skip(2).collect(toList());

		// 5.3 (매핑 - map : 함수를 인수로 받은 결과로 새로운 stream으로 매핑)
		List<Integer> dishNames = menu.stream().map(Dish::getName) // 요리명 추출
				.map(String::length) // 연결(chaining) - 요리명의 길이로 추출
				.collect(toList());

		// 5.3.2 (스트림 평면화)
		// 리스트 중복 없는 문자 출력(map)
		List<String> words = Arrays.asList("Hello", "World");
		words.stream().map(word -> word.split("")) // 문제발생 - Stream<String[]> → ["Hello", "World"]
				.distinct() // 두 String끼리 중복 검사로 인해 중복 발생 X
				.collect(toList()); // 그대로 출력

		// 리스트 중복 없는 문자 출력(flatMap)
		// 1. 문자열을 받아 스트림으로 반환(Arrays.stream())
		String[] arrayofWords = { "Hello", "World" };
		Stream<String> streamOfWords = Arrays.stream(arrayofWords);

		// 새로운 List<> 없이 적용한다면 map(Arrays::stream)이 List<Stream<String>>로 문제 해결이 되지 않는다.
		List<String> unique = words.stream().map(word -> word.split("")) // 각 단어를 개별 문자를 포함하는 배열로 변환
				.flatMap(Arrays::stream) // 생성된 스트림을 하나의 스트림으로 평면화(배열을 스트림으로 변환해주는 메서드 참조 표현)
				.distinct().collect(toList());

		// 5.4 (검색과 매칭)
		// anyMatch(최종연산 - boolean 반환) - menu에 채식요리가 있는지 확인(주어진 Predicate가 적어도 한 요소와
		// 일치하는지 확인)
		if (menu.stream().anyMatch(Dish::isVegetarian)) {
			System.out.println("The menu is (someWhat) vegetarian friendly!!");
		}

		// allMatch(최종연산 - boolean 반환) - 모든 요리가 1000칼로리 미만인지 확인(주어진 Predicate와 일치하는지 확인)
		boolean isHealthy = menu.stream().allMatch(dish -> dish.getCalories() < 1000);

		// noneMatch(최종연산 - boolean 반환) - 주어진 Predicate와 일치하는 요소가 없는지 확인(allMatch와 정반대)
		boolean isHealthy2 = menu.stream().noneMatch(dish -> dish.getCalories() >= 1000);
		// anyMatch, allMatch, noneMatch, limit → 쇼트서킷 기법

		// 5.4.3 (요소 검색)
		// findAny - 가장 먼저 탐색되는 요소 1개 반환(쇼트서킷 기법)
		// Optional<T>는 값이 없을때(null) 어떻게 처리할지 강제하는 기능 제공
		Optional<Dish> dish = menu.stream().filter(Dish::isVegetarian).findAny();// 반환

		// findAny에서 반환된 요리 이름 바로 출력
		dish.ifPresent(d -> System.out.println(d.getName())); // 값이 있으면 출력, 없으면 아무일 X

		// 5.4.4 (첫번째 요소 찾기 - findFirst)
		List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
		// findAny()와 findFirst는 둘다 일치하는 요소 1개를 찾을때 사용하지만 병렬성(parallel)일 경우, 사용성이 달라짐
		Optional<Integer> firstSquareDivisibleByTree = someNumbers.stream().map(n -> n * n).filter(n -> n % 3 == 0)
				.findFirst();

		// 5.5 (리듀싱 - 모든 스트림 요소를 처리해서 값으로 도출)
		// 5.5.1 (요소 합)
		// 자바 8 이전 - numbers리스트에서 모든 숫자를 더하는 reduce 과정 반복
		int sum = 0; // 초깃값
		for (int x : numbers) { // 연산
			sum += x;
		}

		// 자바 8 이후 - reduce(초깃값, 연산)
		int sum2 = numbers.stream().reduce(0, (a, b) -> a + b);
		// 자바 8 이후 - 메서도 참조를 통해 더욱 간결하게 제작 O(Integer 클래스의 정적 sum 메서드)
		int sum3 = numbers.stream().reduce(0, Integer::sum);

		// 초기값이 없는 reduce (stream에 아무 요소도 없는 상황에 대한 오류를 방지하기 위해 Optional 객체로 감싼 결과 반환)
		Optional<Integer> sum4 = numbers.stream().reduce((a, b) -> a + b);

		// 5.5.2 (최댓값 & 최솟값)
		Optional<Integer> max = numbers.stream().reduce(Integer::max);
		Optional<Integer> min = numbers.stream().reduce(Integer::min);

		// 5.7.1
		// reduce대신 sum()으로 직접 호출하면 좋지만 map메서드가 stream을 생성하므로 불가능
		int calories = menu.stream().map(Dish::getCalories) // Stream<T> 생성
				.reduce(0, Integer::sum); // Stream<Integer> → sum 메서드 X

		// 기본형 특화 스트림 (sum, max, min, average 등 지원)
		// 숫자 스트림 매핑
		int calories2 = menu.stream() // Stream<Dish> 반환
				.mapToInt(Dish::getCalories) // IntStream 반환 (Stream<Integer> X)
				.sum(); // 스트림이 비어있으면 0(기본값) 반환

		// 객체 스트림 매핑
		IntStream intStream = menu.stream().mapToInt(Dish::getCalories); // 스트림 → 숫자 스트림으로 변환
		Stream<Integer> stream = intStream.boxed(); // 숫자 스트림 → 스트림으로 변환

		// Optional(Int,Double,Long)
		OptionalInt maxCalories = menu.stream().mapToInt(Dish::getCalories).max();
		int maxi = maxCalories.orElse(1); // 최댓값이 없는 경우 사용할 기본 최댓값 설정 O → 1로 설정

		// 5.7.2 (숫자범위)
		// range - 시작, 종료값이 결과에 포함 X
		IntStream evenNumbers = IntStream.range(1, 100) // 2~99
				.filter(n -> n % 2 == 0);

		// rangeClosed - 시작, 종료값이 결과에 포함 O
		IntStream evenNumbers2 = IntStream.rangeClosed(1, 100) // 1~100
				.filter(n -> n % 2 == 0);

		// 5.8.1 (무한스트림 - Stream.of: 값 지정)
		Stream<String> streamA = Stream.of("Java 8", "Lambdas", "In", "Action");
		streamA.map(String::toUpperCase).forEach(System.out::println);

		// 스트림 비우기
		Stream<String> emptyStream = Stream.empty();

		// 5.8.2 (null이 될 수 있는 객체 스트림 만들기)
		String homeValue = System.getProperty("home"); // 제공된 키에 대응하는 속성 없을 경우 null 반환
		// 위 메서드를 스트림으로 활용하려면 아래와 같이 null에 대한 명시적 확인 필요
		Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(homeValue);

		// homeValue를 한번에 처리 - Stream.ofNullable
//		Stream<String> homeValue2 = Stream.ofNullable(System.getProperty("home"));

//		Stream<String> values = Stream.of("config", "home", "user")
//				.flatMap(key -> Stream.ofNullable(System.getProperty(key)));
		// 5.8.3 (배열로 스트림 - Arrays.stream)
		int[] nums = { 2, 3, 5, 7, 11, 13 };
		int sums = Arrays.stream(nums).sum(); // 41

		// 5.8.4 (파일로 스트림)
		long uniqueWords = 0;

		// File.lines - 파일의 각 행 요소를 반환하는 스트림
//		try (Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
//			uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" "))).distinct() // 중복 제거
//					.count(); // 고유 단어 수 계산
//		} catch (IOException e) {
//			// 파일을 열다가 예외가 발생하면 처리
//			e.printStackTrace();
//		}

		// 5.8.5 (무한 스트림 - infinite stream 또는 언바운드 스트림(unbounded stream))
		// 무한 요청되므로 limit으로 스트림 크기를 제한 해야 한다. 그렇지 않을 경우, 결과 계산 X
		// 연속된 일련의 값 - iterate
		Stream.iterate(0, n -> n + 2) // 초깃값, 람다
				.limit(10) // 스트림의 크기를 명시적 제한
				.forEach(System.out::println);

		// filter 메서드는 언제 이 작업을 중단해야 하는지를 알 수 없기 때문에 이 예제에서는 사용이 불가하다.
		// Predicate를 지원(자바 9 이상)
//		IntStream.iterate(0, n -> n < 100, n -> n + 4) // 초깃값, Predicate(작업을 언제까지 수행?), 람다
//				.forEach(System.out::println);

		// takeWhile (스트림 쇼트서킷을 지원)
//		IntStream.iterate(0, n -> n + 4).takeWhile(n -> n < 100).forEach(System.out::println);

		// iterate와 달리 Supplier<T>(발행자(supplier, 즉 메서드 참조 Math.random)는 상태가 없는 메서드)
		// 를 인수로 받아서 새로운 값을 생산 → 나중 계산에 사용할 어떤 값도 저장해두지 않는다는 것
		Stream.generate(Math::random) // Math.random : 임의의 새로운 값을 생성
				.limit(5) // limit가 없다면 스트림은 언바운드 상태가 됨
				.forEach(System.out::println);

		// 피보나치 수열 출력
		// iterate(불변 상태) - 스트림을 병렬로 처리하면서 올바른 결과를 얻기 위해서는 불변 상태 기법을 고수해야 함
		Stream.iterate(new int[] { 0, 1 }, t -> new int[] { t[1], t[0] + t[1] }).limit(20).map(t -> t[0])
				.forEach(System.out::println);

		// generate(가변 상태) - 발행자가 상태를 저장 후 다음 값을 만들 때 상태 고침 →
		// 발행자는 안전 X, 실제로 이런 코드는 피해야함
		IntSupplier fib = new IntSupplier() { // 익명 클래스를 사용
			// getAsInt 메서드의 연산을 커스터마이즈할 수 있는 상태 필드를 정의
			private int previous = 0;
			private int current = 1;

			@Override
			public int getAsInt() {
				int nextValue = previous + current;
				previous = current;
				current = nextValue; // 상태를 갱신
				return previous;
			}
		};

		IntStream.generate(fib).limit(10).forEach(System.out::println);
	}
}
