package Chap07;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreams {
	public static void main(String[] args) {
		// 7.1 (병렬 스트림: 각각의 스레드에서 처리할 수 있도록 스트림 요소를 여러 청크로 분할한 스트림 - parallelStream)
		// 병렬 스트림은 내부적으로 ForkJoinPool사용 → Runtime.getRuntime().availableProcessors()가
		// 반환하는 값에 상응하는 스레드를 갖는다.
		// 전역 설정 코드로, 모든 병렬 스트림 연산에 영향을 줌
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");

		// 7.2.1 (Recursive Task 활용)
		// pseudo code - task를 서브태스크로 분할하는 로직과 더 이상 분할할 수 없을 때 개별 서브태스크의 결과를 생산할 알고리즘을
		// 정의 → 분할 후 정복(divide-and-conquer) 알고리즘의 병렬화 버전
//		if (태스크가 충분히 작거나 더 이상 분할할 수 없으면) {
//			순차적으로 태스크 계산
//		} else {
//			태스크를 두 서브태스크로 분할
//			태스크가 다시 서브태스크로 분할되도록 이 메서드를 재귀적으로 호출함
//			모든 서브태스크의 연산이 완료될 때까지 기다림
//			각 서브태스크의 결과를 합침
//		}

		// 7.3 Spliterator(splitable iterator, 분할할 수 있는 반복자) 인터페이스
		// iterator과 같이 요소 탐색 기능 제공 + 병렬 작업 특화

	}

	// 7.1.1
	// n을 인수로 1~n까지 모든 숫자의 합계 반환 메서드(순차 스트림)
	public static long sequentialSum(long n) {
		return Stream.iterate(1L, i -> i + 1) // 무한 자연수 스트림 생성
				.limit(n).reduce(Long::sum) // 스트림 리듀싱 연산
				.get();
	}

	// 병렬 스트림 → 리듀싱 연산이 병렬로 처리(여러 청크로 나눠 부분 결과들을 합쳐서 전체 스트림 결과를 도출)
	public static long sequentialSum2(long n) {
		return Stream.iterate(1L, i -> i + 1) // 무한 자연수 스트림 생성
				.limit(n).parallel() // 스트림을 병렬 스트림으로 변환 → sequential()을 사용하면 순차스트림으로 변경
				.reduce(Long::sum) // 스트림 리듀싱 연산
				.get();
	}

	// ForkJoinSumCalculator를 위한 ParallelStreamsHarness값을 구하는 메서드
	public static long iterativeSum(long n) {
		long result = 0;
		for (long i = 0; i <= n; i++) {
			result += i;
		}
		return result;
	}

	public static long parallelSum(long n) {
		return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(Long::sum).get();
	}

	public static long rangedSum(long n) {
		return LongStream.rangeClosed(1, n).reduce(Long::sum).getAsLong();
	}

	public static long parallelRangedSum(long n) {
		return LongStream.rangeClosed(1, n).parallel().reduce(Long::sum).getAsLong();
	}

	public static long sideEffectSum(long n) {
		Accumulator accumulator = new Accumulator();
		LongStream.rangeClosed(1, n).forEach(accumulator::add);
		return accumulator.total;
	}

	public static long sideEffectParallelSum(long n) {
		Accumulator accumulator = new Accumulator();
		LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
		return accumulator.total;
	}

	public static class Accumulator {

		private long total = 0;

		public void add(long value) {
			total += value;
		}

	}
}
