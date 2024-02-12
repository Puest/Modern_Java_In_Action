package Chap18;

import java.util.stream.LongStream;

public class Recursion {
	public static void main(String[] args) {
		System.out.println(factorialIterative(5));
		System.out.println(factorialRecursive(5));
		System.out.println(factorialTailRecursive(5));
		System.out.println(factorialStreams(5));
	}

	// 반복 방식의 팩토리얼 (매 반복마다 변수 r과 i가 갱신됨)
	public static int factorialIterative(int n) {
		int r = 1;

		for (int i = 1; i <= n; i++) {
			r *= i;
		}

		return r;
	}

	// 재귀 방식의 팩토리얼 (갱신 X, 변화 X → 최종 연산 : n과 재귀 호출의 결과값의 곱셈)
	// (재귀)함수 호출 → 새로운 스택 → 스택 프레임 생성 → 비용 expensive
	public static long factorialRecursive(int n) {
		return n == 1 ? 1 : n * factorialRecursive(n - 1);
	}

	// 꼬리 호출 최적화 (재귀 방식에 대한 함수형 언어 해결책)
	public static long factorialTailRecursive(long n) {
		return factorialHelper(1, n);
	}

	// 꼬리 재귀 (재귀 호출이 가장 마지막에서 이루어짐)
	public static long factorialHelper(long acc, long n) {
		return n == 1 ? acc : factorialHelper(acc * n, n - 1);
	}

	// 스트림 팩토리얼 (반복을 스트림으로 대체해서 변화를 피함)
	public static long factorialStreams(long n) {
		return LongStream.rangeClosed(1, n).reduce(1, (long a, long b) -> a * b);
	}
}
