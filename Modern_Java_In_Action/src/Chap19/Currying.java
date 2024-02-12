package Chap19;

import java.util.function.DoubleUnaryOperator;

// 19.1.2 (커링 : 두 인수를 받는 함수 f를 한개의 인수를 받는 g 함수로 대체하는 기법)
public class Currying {
	public static void main(String[] args) {
		// 아래 메서드에 변환 요소(f)와 기준치(b)만 넘겨주면 원하는 작업을 수행할 함수가 반환됨
		DoubleUnaryOperator convertCtoF = curriedConverter(9.0 / 5, 32);
		DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);

		System.out.printf("24 °C = %.2f °F%n", convertCtoF.applyAsDouble(24));
		System.out.printf("US$100 = £%.2f%n", convertUSDtoGBP.applyAsDouble(100));
	}

	// x: 변환하려는 값, f: 변환 요소, b: 기준치 조정 요소
	public static double converter(double x, double y, double z) {
		return x * y + z;
	}

	// f: 변환 요소, b: 기준치 조정 요소
	public static DoubleUnaryOperator curriedConverter(double f, double b) {
		return (double x) -> x * f + b;
	}
}
