package Chap08;

import static java.util.stream.Collectors.toList;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WorkingWithCollections {
	// 거래 목록
	public static List<Transaction> transactions = Arrays.asList(new Transaction(Currency.EUR, 1500.0),
			new Transaction(Currency.USD, 2300.0), new Transaction(Currency.GBP, 9900.0),
			new Transaction(Currency.EUR, 1100.0), new Transaction(Currency.JPY, 7800.0),
			new Transaction(Currency.CHF, 6700.0), new Transaction(Currency.EUR, 5600.0),
			new Transaction(Currency.USD, 4500.0), new Transaction(Currency.CHF, 3400.0),
			new Transaction(Currency.GBP, 3200.0), new Transaction(Currency.USD, 4600.0),
			new Transaction(Currency.JPY, 5700.0), new Transaction(Currency.EUR, 6800.0));

	// 통화
	public enum Currency {
		EUR, USD, JPY, GBP, CHF
	}

	private MessageDigest messageDigest;

	public static void main(String[] args) {
		// 8.1 (컬렉션 팩토리)
		// 리스트 생성
		List<String> abc = Arrays.asList("abc");
		// 예외(UnsupportedOperationException ) 발생 : 내부적으로 고정된 크기의 변환할 수 있는 배열로 구현 →
		// 위 List는 요소를 갱신하는 작업 OK 하지만, 요소를 추가(add)하거나 변경(set), 삭제(remove)하는 작업 X
//		abc.add("de");

		// 8.1.3 (맵<키,값> 팩토리)
		// 맵 초기화(1회) - Map.of, Map.ofEntries(ImmutableCollections 를 반환)
		Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);
		// entry 메서드는 Map.Entry 객체를 만드는 팩토리 메서드다.
		Map.ofEntries(Map.entry("a", 1), Map.entry("b", 2), Map.entry("c", 3));

		// 8.2.1 (removeIf 메서드)
//		for (Transaction transaction : transactions) {
//			if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
//				transactions.remove(transaction);
//			}
//		}
		// → 위 코드의 실직적인 코드 (for-each 루프는 Iterator 객체를 사용)
		// Iterator의 상태와 transactions의 상태는 서로 동기화되지 않아 오류를 발생
//		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
//			Transaction transaction = iterator.next();
//			if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
//				transactions.remove(transaction);
//			}
//		}

		// 해결 방안 1. Iterator에서 직접 삭제 → 코드가 복잡
//		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
//			Transaction transaction = iterator.next();
//			if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
//				iterator.remove();
//			}
//		}
		// 해결 방안 2. removeIf 메서드 사용(Predicate<T>를 인수로 받음)
//		transactions.removeIf(transaction -> Character.isDigit(transaction.getReferenceCode().charAt(0)));

		// 8.2.2 (replaceAll 메서드)
		List<String> referenceCodes = Arrays.asList("a12", "C14", "b13");
		referenceCodes.stream().map(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1)).collect(toList())
				.forEach(System.out::println);
		// 스트림의 map과 비슷
		referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));

		// 8.3.1 (forEach 메서드 - map의 키, 값을 반복하는 작업에 사용)
		Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
		// foreEach 메서드 사용
		// entrySet(): Key와 Value의 값 모두 출력
		for (Map.Entry<String, Integer> entry : ageOfFriends.entrySet()) {
			String friend = entry.getKey();
			Integer age = entry.getValue();
			System.out.println(friend + " is " + age + " years old");
		}

		// foreEach 메서드 사용 → BiCousumer<T,U>를 인수로 받는다.
//		ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));

		// 8.3.2 (정렬 메서드 - Entry.comparingByKey(),Entry.comparingByValue()로 상황에 따라 정렬 O)
		Map<String, String> favoriteMovies = Map.ofEntries(Map.entry("Raphael", "Star Wars"),
				Map.entry("Cristina", "Matrix"), Map.entry("Olivia", "James Bond"));
		favoriteMovies.entrySet().stream().sorted(Entry.comparingByValue());
		// Key: {Cristina=Matrix, Olivia=James Bond, Raphael=Star Wars}
		// Value: {Raphael=Star Wars, Olivia=James Bond, Cristina=Matrix}
		System.out.println(favoriteMovies);

		// 8.3.3 (getOrDefault 메서드 - 키가 존재 X → 결과가 null → 이 메서드가 null대신 대체값을 지정해 문제 해결)
		Map<String, String> favoriteMovies2 = Map.ofEntries(Map.entry("Raphael", "Star Wars"),
				Map.entry("Cristina", "Matrix"), Map.entry("Olivia", "James Bond"));
		// James Bond 출력
		System.out.println(favoriteMovies2.getOrDefault("Olivia", "Matrix"));
		// Thibaut은 존재하지 않는 Key로 Value는 지정한 대체값 Matrix 출력
		System.out.println(favoriteMovies2.getOrDefault("Thibaut", "Matrix"));

		// 8.3.4
		new WorkingWithCollections().main();

		// 키 O → 영화 리스트를 반환하여 반환된 리스트에 add, 키 X → 새로운 리스트에 add
		Map<String, List<String>> friendsToMovies = new HashMap<>();
		friendsToMovies.computeIfAbsent("Raphael", name -> new ArrayList<>()).add("Star Wars");

		// 8.3.5
		removingFromMaps();

		// 8.3.6
		replacingInMaps();

		// 8.3.7
		mergingMaps();

		// 8.4 (개선된 ConcurrentHashMap → 동시성 친화적, 내부 자료구조의 특정 부분만 잠궈 동시 추가+갱신 작업 허용,
		// HashTable 버전에 비해 읽기 쓰기 연산 능력이 월등)
		ConcurrentHashMap<String, Long> map1 = new ConcurrentHashMap<>();

		// 병렬성 기준값
		long parallelismThreshold = 1;
		// 최대값을 찾는다.
		Optional<Long> maxValue = Optional.ofNullable(map1.reduceValues(parallelismThreshold, Long::max));
		System.out.println(maxValue);
	}

	// 8.3.4 (계산 패턴 - Key 존재 여부에따라 동작을 실행, 결과를 저장하는 상황이 필요할때 사용)
	public WorkingWithCollections() {
		// 각 라인을 SHA-256의 해시 값으로 계산해서 저장하기 위한 계산 객체 생성
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void main() {
		// 리스트
		List<String> lines = Arrays.asList(" Nel   mezzo del cammin  di nostra  vita ",
				"mi  ritrovai in una  selva oscura", " che la  dritta via era   smarrita ");
		// 캐시
		Map<String, byte[]> dataToHash = new HashMap<>();

		// 키가 없으면 line과 계산된 해시 값이 Key, Value로 들어감
		lines.forEach(line -> dataToHash.computeIfAbsent(line, this::calculateDigest));

		dataToHash.forEach((line, hash) -> System.out.printf("%s -> %s%n", line, new String(hash).chars()
				.map(i -> i & 0xff).mapToObj(String::valueOf).collect(Collectors.joining(", ", "[", "]"))));
	}

	// 키의 해시를 계산해서 반환
	private byte[] calculateDigest(String key) {
		return messageDigest.digest(key.getBytes(StandardCharsets.UTF_8));
	}

	// 8.3.5 (삭제 패턴 - remove)
	private static void removingFromMaps() {
		// 자바 8 이전
		// 바꿀 수 없는 맵 필요
		Map<String, String> favouriteMovies = new HashMap<>();
		favouriteMovies.put("Raphael", "Jack Reacher 2");
		favouriteMovies.put("Cristina", "Matrix");
		favouriteMovies.put("Olivia", "James Bond");
		String key = "Raphael";
		String value = "Jack Reacher 2";

		boolean result = remove(favouriteMovies, key, value);

		// 자바 8 이후
		favouriteMovies.put("Raphael", "Jack Reacher 2"); // 테스트를 위해 삭제된 항목 다시 입력

		favouriteMovies.remove(key, value);
		System.out.printf("%s [%b]%n", favouriteMovies, result);
	}

	// 특정 키가 포함되고 키의 값이 일치하면 제거
	private static <K, V> boolean remove(Map<K, V> favouriteMovies, K key, V value) {
		if (favouriteMovies.containsKey(key) && Objects.equals(favouriteMovies.get(key), value)) {
			favouriteMovies.remove(key);
			return true;
		} else {
			return false;
		}
	}

	// 8.3.6 (교체 패턴 - 맵의 항목을 바꾸는데 사용)
	private static void replacingInMaps() {
		Map<String, String> favouriteMovies = new HashMap<>();
		favouriteMovies.put("Raphael", "Star Wars");
		favouriteMovies.put("Olivia", "james bond");

		// replaceAll: BiFunction을 적용한 결과로 각 항목의 값을 교체
		// replace: 키(key)가 존재하면 맵의 값(value)을 변경. 키가 특정 값으로 매핑시에만 값을 교체하는 오버로드 버전도 존재
		favouriteMovies.replaceAll((friend, movie) -> movie.toUpperCase());
		System.out.println(favouriteMovies);
	}

	// 8.3.7 (합침 - putAll)
	private static void mergingMaps() {
		Map<String, String> family = Map.ofEntries(Map.entry("Teo", "Star Wars"), Map.entry("Cristina", "James Bond"));
		Map<String, String> friends = Map.ofEntries(Map.entry("Raphael", "Star Wars"));

		Map<String, String> everyone = new HashMap<>(family);
		everyone.putAll(friends);
		System.out.println(everyone);

		// merge
		// 중복된 키가 있다면 두 값을 연결
		// 중복되지 않는다면 즉, everyone의 get(k) 값이 null이라면 k,v 그대로 저장
		Map<String, String> everyone2 = new HashMap<>(family);
		friends.forEach((k, v) -> everyone2.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));
		System.out.println(everyone2);

		// merge
		// 초기화 검사
		// 원하는 값이 초기화 되어 있다면 +1
		// 초기화 되어있지 않아 null이라면 moviename, 1 저장
//		moviesToCount.merge(movieName, 1L, (key, count) -> count + 1L);
	}

	public static class Transaction {

		private final Currency currency;
		private final double value;
		private String referenceCode;

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

		public String getReferenceCode() {
			return referenceCode;
		}

		@Override
		public String toString() {
			return currency + " " + value;
		}
	}

}
