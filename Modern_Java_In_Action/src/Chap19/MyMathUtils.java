package Chap19;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// 19.3 (스트림과 게으른 평가)
// 19.3.1 (자기 정의 스트림 - 소수 생성 예제)
public class MyMathUtils {
	public static void main(String[] args) {
		System.out.println(primes(25).map(String::valueOf).collect(Collectors.joining(", ")));
	}

	public static Stream<Integer> primes(int n) {
		return Stream.iterate(2, i -> i + 1) // 스트림 숫자 얻기
				.filter(MyMathUtils::isPrime).limit(n);
	}

	public static boolean isPrime(int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate);
		return IntStream.rangeClosed(2, candidateRoot).noneMatch(i -> candidate % i == 0);
	}

}
