package Chap09;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

// 9.4.2 (정보 로깅)
public class Peek {
	// 스트림의 파이프라인 연산을 로깅할 때 스트림 파이프라인에 적용된 각각의 연산이 어떤 결과를 도출하는지 알기 위해
	// peek이라는 스트림 연산을 활용
	public static void main(String[] args) {
		List<Integer> result = Stream.of(2, 3, 4, 5)// peek은 소비한 것처럼 동작하나 실제로 소비 X
				.peek(x -> System.out.println("from stream: " + x)).map(x -> x + 17)
				.peek(x -> System.out.println("after map: " + x)).filter(x -> x % 2 == 0)
				.peek(x -> System.out.println("after filter: " + x)).limit(3)
				.peek(x -> System.out.println("after limit: " + x)).collect(toList());
	}

}
