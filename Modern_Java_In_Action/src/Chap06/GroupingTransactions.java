package Chap06;

import static Chap06.Dish.dishTags;
import static Chap06.Dish.menu;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GroupingTransactions {
	// 거래 목록
	public static List<Transaction> transactions = Arrays.asList(new Transaction(Currency.EUR, 1500.0),
			new Transaction(Currency.USD, 2300.0), new Transaction(Currency.GBP, 9900.0),
			new Transaction(Currency.EUR, 1100.0), new Transaction(Currency.JPY, 7800.0),
			new Transaction(Currency.CHF, 6700.0), new Transaction(Currency.EUR, 5600.0),
			new Transaction(Currency.USD, 4500.0), new Transaction(Currency.CHF, 3400.0),
			new Transaction(Currency.GBP, 3200.0), new Transaction(Currency.USD, 4600.0),
			new Transaction(Currency.JPY, 5700.0), new Transaction(Currency.EUR, 6800.0));

	public static void main(String[] args) {
		// 6.1 (컬렉터)
		// 통화별로 그룹화 - 명령형 프로그래밍 버전 → 무엇을 실행하는지 한눈에 파악 어려움
		Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();

		for (Transaction transaction : transactions) {
			Currency currency = transaction.getCurrency(); // transactions 통화(Currency) 정보 가져옴
			List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
			if (transactionsForCurrency == null) {
				transactionsForCurrency = new ArrayList<>();
				transactionsByCurrencies.put(currency, transactionsForCurrency);
			}
			transactionsForCurrency.add(transaction);
		}
		// 함수형 프로그래밍 버전
		Map<Currency, List<Transaction>> transactionsByCurrencies2 = transactions.stream()
				// collect: 스트림 요소를 어떤 식으로 도출할지에 대해 지정
				// groupingBy: 키(통화) 버킷(bucket) 그리고 각 키 버킷에 대응하는 요소 리스트를 값으로 포함하는 맵(Map)을 만드는 동작
				.collect(groupingBy(Transaction::getCurrency));

		// 6.2 (리듀싱과 요약)
		// 6.1.1 (최댓값(maxBy), 최솟값(minBy))
		Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
		Optional<Dish> mostCaloriesDish = menu.stream().collect(Collectors.maxBy(dishCaloriesComparator));

		// 6.2.2 (요약 연산)
		// 합(summing - Int, Long, Double)
		int totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));

		// 평균값(averaging - Int, Long, Double)
		double avgCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));

		// 모든 요소 수(count), 합계(sum), 평균(average), 최댓값(max), 최솟값(min)을 한번에 수행 (summarizing
		// - Int, Long, Double)
		IntSummaryStatistics menuStatistics = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));

		// 6.2.3 (문자열 연결 - joining → 내부적으로 StringBuilder를 이용해 문자열을 하나로 만듦)
		// 모든 요리명 연결
		String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining());

		// Dish에 요리명을 반환하는 toString메서드가 있다면 아래와 같이 수정 O
//		String shortMenu = menu.stream().collect(joining());

		// 두 요소 사이 구분 문자열 O
		String shortMenu2 = menu.stream().map(Dish::getName).collect(Collectors.joining(","));

		// 6.2.4 (범용 리듀싱 요약 연산 - reducing 팩토리 메서드(범용 Collectors.reducing)로도 위에 예제 정의 O)
		// 특화된 컬렉션(6.1~6.2.3) 사용한 이유: 편의성 + 예제연습
		// 모든 메뉴의 칼로리 합계(reducing - 3개 인수: 시작값(스트림에 인수 없으면 반환값),변환 함수, 공식)
		int totalCalories2 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));

		// 컬렉션 프레임워크 유연성 : 같은 연산도 다양한 방식으로 수행 O
		int totalCalories3 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, Integer::sum));
		// 인수들은 순서대로 '초깃값, 변환 함수, 합계 함수'이다.

		// 가장 칼로리가 높은 요리(한 개의 인수만 가지는 reducing도 존재: 요소, 항등함수 → 시작값 X)
		Optional<Dish> mostCaloriesDish2 = menu.stream()
				.collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));

		// 6.3 (그룹화 - groupingBy(분류 함수): 하나 이상의 특성으로 분류해서 그룹화 → 반환값: Map)
		Map<Dish.Type, List<Dish>> dishType = menu.stream().collect(groupingBy(Dish::getType));

		// 그룹화 - 람다 표현식 → 필요한 로직 구현 O
		Map<CaloricLevel, List<Dish>> dishByCaloricLevel = menu.stream().collect(groupingBy(dish -> {
			if (dish.getCalories() <= 400) {
				return CaloricLevel.DIET;
			} else if (dish.getCalories() <= 700) {
				return CaloricLevel.NORMAL;
			} else {
				return CaloricLevel.FAT;
			}
		}));

		// 6.3.1
		// 500 칼로리가 넘는 요리만 필터 → FISH 종류가 없기 때문에 키 자체가 사라짐(문제 발생)
		Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream().filter(dish -> dish.getCalories() > 500)
				.collect(groupingBy(Dish::getType));

		// 해결 방안 groupingBy 두번째 인수를 추가
		Map<Dish.Type, List<Dish>> caloricDishesByType2 = menu.stream()
				.collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));

		// mapping - 각 항목에 적용한 함수를 모으는 데 사용하는 또 다른 컬렉션을 인수로 받음
		// 그룹의 각 요리를 관련 이름 목록으로 변환
		Map<Dish.Type, List<String>> dishNameByType = menu.stream()
				.collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));

		// flatMapping
		Map<Dish.Type, Set<String>> dishNameByType2 = menu.stream().collect(
				// toSet() - 집합으로 그룹화 -> 중복 태그를 제거
				groupingBy(Dish::getType, flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet())));

		// 6.3.2 (다수준 그룹화 - groupingBy(groupingBy()) )
		Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream()
				.collect(groupingBy(Dish::getType, // 첫 번째 수준의 분류 함수
						groupingBy(dish -> { // 두 번째 수준의 분류 함수
							if (dish.getCalories() <= 400) {
								return CaloricLevel.DIET;
							} else if (dish.getCalories() <= 700) {
								return CaloricLevel.NORMAL;
							} else {
								return CaloricLevel.FAT;
							}
						})));

		// 6.3.3 서브그룹(두번째 인수)으로 데이터 수집
		// counting
		Map<Dish.Type, Long> typecount = menu.stream().collect(groupingBy(Dish::getType, counting()));

		// groupingBy(f) == groupingBy(f,toList()) 즉, 축약형
		Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream()
				.collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));

		// collectingAndThen - 컬렉터가 반환한 결과를 다른 형식에 적용
		Map<Dish.Type, Dish> mostCaloricByType2 = menu.stream().collect(groupingBy(Dish::getType, // 분류 함수
				collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)) // 변환 함수: Optional에 포함된 값을 추출함
		);

		// groupingBy + mapping 컬렉터
		Map<Dish.Type, Set<CaloricLevel>> li = menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
			if (dish.getCalories() <= 400) {
				return CaloricLevel.DIET;
			} else if (dish.getCalories() <= 700) {
				return CaloricLevel.NORMAL;
			} else {
				return CaloricLevel.FAT;
			}
		}, toSet())));

		// toCollection - 원하는 형식으로 결과를 제어 O
		Map<Dish.Type, Set<CaloricLevel>> liw = menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
			if (dish.getCalories() <= 400) {
				return CaloricLevel.DIET;
			} else if (dish.getCalories() <= 700) {
				return CaloricLevel.NORMAL;
			} else {
				return CaloricLevel.FAT;
			}
		}, toCollection(HashSet::new))));

		// 6.4 (분할)
		// 모든 요리를 채식(O,X)으로 분류
		Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian)); // 분할
																															// 함수

		// 6.4.1 (분할의 장점 - 참, 거짓 두 가지 요소의 스트림 리스트를 모두 유지)
		// 컬렉터를 두 번째 인수로 전달할 수 있는 오버로드된 버전
		Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType = menu.stream()
				.collect(Collectors.partitioningBy(Dish::isVegetarian, // 분할 함수
						groupingBy(Dish::getType))); // 두 번째 컬렉터

		// 채식 요리or아닌 요리의 각각 그룹에서 가장 칼로리가 높은 요리 찾기
		Map<Boolean, Dish> mostCaloricPartitionedByVegetarian = menu.stream()
				.collect(Collectors.partitioningBy(Dish::isVegetarian,
						Collectors.collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)));

		// 6.5.2 (응용)
		// 기존 코드
		List<Dish> dishes = menu.stream().collect(toList()); // toList는 팩토리

		// ToListCollector는 new로 인스턴스화 함
//		List<Dish> dishes = menu.stream().collect(new ToListCollector<>());

	}

	public static class Transaction {

		private final Currency currency;
		private final double value;

		public Transaction(Currency currency, double value) {
			this.currency = currency;
			this.value = value;
		}

		public Currency getCurrency() {
			return currency;
		}

		public double getValue() {
			return value;
		}

		@Override
		public String toString() {
			return currency + " " + value;
		}

	}

	// 통화
	public enum Currency {
		EUR, USD, JPY, GBP, CHF
	}

	//
	public enum CaloricLevel {
		// DIET : 400 칼로리 이하
		// NORMAL : 400~700 칼로리
		// FAT : 700 칼로리 초과
		DIET, NORMAL, FAT
	}

	// 6.4.2 (숫자를 소수와 비소수로 분할)
	// 소수인지 판단하는 Predicate
	public boolean isPrime(int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate); // 소수 대상을 주어진 수의 제곱근 이하의 수로 제한
		return IntStream.rangeClosed(2, candidateRoot) // 범위 포함 자연수 생성
				.noneMatch(i -> candidate % i == 0); // candidate를 나눌 수 없으면 참을 반환
	}

	// pratitionPrimes 컬렉터
	public Map<Boolean, List<Integer>> partitionPrimes(int n) {
		return IntStream.rangeClosed(2, n).boxed().collect(Collectors.partitioningBy(candidate -> isPrime(candidate)));
	}

	// 6.5.1 (collector 인터페이스 메서드)
	// stream의 모든 요소를 List로 수집하는 클래스 구현
	public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
		// supplier(빈 누적자 인스턴스를 만드는 즉, 파라미터가 없는 함수 반환) 메서드 : 새로운 결과 컨테이너 만들기
		// 빈 결과로 아루어진 리스트 반환
		@Override
		public Supplier<List<T>> supplier() {
			// 람다 표현식
//			return () -> new ArrayList<T>();

			// 생성자 참조 방식
			return ArrayList::new; // 수집 연산의 시작점
		}

		// accumulator(리듀싱 연산을 수행하는 함수 반환(void)) 메서드 : 결과 컨테이너에 요소 추가하기
		@Override
		public BiConsumer<List<T>, T> accumulator() {
			// 람다 표현식
//			return (list, item) -> list.add(item);

			// 생성자 참조 방식
			return List::add; // 탐색한 항목을 누적하고 누적자를 고침
		}

		// finisher(누적 과정을 끝낼 때 호출할 함수 반환) 메서드 : 최종 변환값을 결과 컨테이너로 적용하기
		// 누적자 객체가 이미 최종 결과 → 변환 과정 필요 X → finisher 항등함수 반환
		@Override
		public Function<List<T>, List<T>> finisher() {
			return Function.identity(); // 항등 함수
		}
		// 위 세가지 메서드를 통해 순차적 스트림 리듀싱 기능을 수행 O

		// combiner(리듀싱 연산에서 사용할 함수를 반환) 메서드 : 두 결과 컨테이너 병합
		// 즉, stream의 서로 다른 서브파트를 병렬로 처리할 때 누적자가 이 결과를 어떻게 처리할지 정의 → 이 메서드를 통해 리듀싱을 병렬로
		// 수행 O
		@Override
		public BinaryOperator<List<T>> combiner() {
			return (list1, list2) -> { // 두 번째 콘텐츠와 합쳐서 첫 번째 누적자를 고치
				list1.addAll(list2); // 변경된 첫 번째 누적자를 반환
				return list1;
			};
		}

		// characteristics(컬렉터의 연산을 정의하는 Characteristics 형식의 불변 집합을 반환) 메서드
		// stream을 병렬로 reduce할 것인지, 어떤 최적화를 선택할지 힌트 제공
		@Override
		public Set<Characteristics> characteristics() {
			return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT));
		}

	}

}
