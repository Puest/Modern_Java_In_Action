package Chap09;

import java.util.Arrays;
import java.util.List;

// 9.4 (디버깅)
public class Debugging {
	public static void main(String[] args) {
		// 9.4.1 (스택 트레이스 확인)
		List<Point> points = Arrays.asList(new Point(12, 2), null);// null 값을 리스트에 넣음(람다 표현식 내부에서 에러가 발생)
		points.stream().map(p -> p.getX()).forEach(System.out::println); // 에러 발생(람다)
		points.stream().map(Point::getX).forEach(System.out::println); // 에러 발생(메서드 참조)

		// 메서드 참조를 사용하는 클래스와 같은 곳에 선언되어 있는 메서드를 참조할 경우 메서드 참조 이름이 스택트레이스에 나타남
		List<Integer> numbers = Arrays.asList(1, 2, 3);
		numbers.stream().map(Debugging::divideByZero).forEach(System.out::println);
	}

	public static int divideByZero(int n) {
		return n / 0;
	}

	private static class Point {

		private int x;
		private int y;

		private Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

	}
}
