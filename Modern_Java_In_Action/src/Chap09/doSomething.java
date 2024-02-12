package Chap09;

import static Chap09.Dish.menu;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

public class doSomething {
	private static final String FILE = doSomething.class.getResource("./data.txt").getFile();

	public enum CaloricLevel {
		// DIET : 400 칼로리 이하
		// NORMAL : 400~700 칼로리
		// FAT : 700 칼로리 초과
		DIET, NORMAL, FAT
	}

	public static void main(String[] args) throws IOException {

		// 9.1.2 (익명 클래스를 람다 표현식으로 리팩터링)
		// 2번 예시 - 익명 클래스는 감싸고 있는 클래스의 변수를 가릴 수 있다(섀도 변수)

		int a = 10;
		Runnable r1 = () -> {
//			int a = 2; // 컴파일 에러(람다는 Shadow 변수를 사용할 수 없음)
			System.out.println(a);
		};

		// 익명 클래스를 사용한 이전 코드
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				int a = 20; // 정상 동작
				System.out.println(a);
			}
		};

		//
		// Task를 구현하는 익명 클래스를 이렇게 명시적으로 전달할 수 있다.
		doSomething(new Task() {
			public void execute() {
				System.out.println("Danger danger!!");
			}
		});
		// 람다 표현식으로는 어떤 인터페이스(Runnable, Task)를 사용하는지 알 수 X → 문제 발생
//		doSomething(() -> System.out.println("Danger danger!!"));
		// 명시적으로 형변환을 통해 모호함을 제거 O
		doSomething((Task) () -> System.out.println("Danger danger!!"));

		// 9.1.3 (람다 표현식을 메서드 참조로 리팩터링)
		// 람다
		Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(groupingBy(dish -> {
			if (dish.getCalories() <= 400) {
				return CaloricLevel.DIET;
			} else if (dish.getCalories() <= 700) {
				return CaloricLevel.NORMAL;
			} else {
				return CaloricLevel.FAT;
			}
		}));
		// 메서드 참조(Dish 클래스 내부에 CaloricLevel의 getCaloriclLevel메서드를 생성)
		Map<CaloricLevel, List<Dish>> dishesByCaloricLevel2 = menu.stream().collect(groupingBy(Dish::getCaloricLevel));

		// sort 메서드 참조
		List<Apple> inventory = Arrays.asList(new Apple(80, Color.GREEN), new Apple(155, Color.GREEN),
				new Apple(120, Color.RED));
		inventory.sort(comparing(Apple::getWeight));

		// reducing 연산 메서드 참조
		int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));

		// 9.1.4 (명령형 데이터 처리를 스트림으로 리팩터링)
		// 명령형
		List<String> dishNames = new ArrayList<>();
		for (Dish dish : menu) {
			if (dish.getCalories() > 300) {
				dishNames.add(dish.getName());
			}
		}
		// 스트림(더 직접적으로 기술 + 병렬화)
		menu.stream().filter(d -> d.getCalories() > 300).map(Dish::getName).collect(toList());

		// 9.1.5 (코드 유연성 개선 - 람다 사용 → 동작 파라미터 구현 = 코드 유연성 대폭 개선)
		// 조건부 연기 실행 - 실제 작업 처리 코드 내부에 제어 흐름문이 복잡하게 얽힌 것을 볼 수 있음
		// logger의 상태가 isLoggable이라는 메서드에 의해 클라이언트 코드로 노출됨
		// logger의 상태를 매번 확인해야함
		// 로거 생성
		Logger logger = Logger.getLogger(doSomething.class.getName());

		// 예제 로그 메시지 생성 메서드
		String generateDiagnostic = "This is a log message.";
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Problem: " + generateDiagnostic);
		}

		// 개선 1 - logger가 활성화 되어 있지 않더라도 항상 로깅 메시지를 평가
		logger.log(Level.FINER, "Problem: " + generateDiagnostic);

		// 개선 2 - 람다를 통해 특정 조건에만 메시지 생성될 수 있도록 생성과정 연기(자바 8에 추가된 log메서드 시그니쳐)
		logger.log(Level.FINER, () -> "Problem: " + generateDiagnostic);

		// 실행 어라운드 - 매번 같은 준비,종료 과정을 반복적으로 수행하는 코드 → 람다로 변환(중복 ↓)
		String oneLine = processFile((BufferedReader b) -> b.readLine());
		String twoLines = processFile((BufferedReader b) -> b.readLine() + b.readLine());

	}

	// 9.1.2
	private String str = "hello";

	// 1번 예시 - 익명 클래스에서 사용한 this와 super는 람다 표현식에서 다른 의미를 갖는다.
	private void testScope() {
		Runnable run1 = new Runnable() {
			String str = "world";

			@Override
			public void run() {
				System.out.println(this.str); // 익명 클래스 this → 익명 클래스 내에서 scope 즉, "world"
			}
		};
		run1.run();

		// 람다 클래스 this → wrapping class 즉, "hello"
		Runnable run2 = () -> System.out.println(this.str);
		run2.run();
	}

	// 3번 예시 - 익명 클래스를 람다 표현식으로 변경하면 콘텍스트 오버로딩에 따른 모호함이 초래될 수 있다
	interface Task {
		public void execute();
	}

	// 오버로딩
	public static void doSomething(Runnable r) {
		r.run();
	}

	public static void doSomething(Task a) {
		a.execute();
	}

	// 9.1.5
	public static String processFile(BufferedReaderProcessor p) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
			return p.process(br); // 인수로 전달된 BufferedReaderProcessor를 실행
		}
	}

	public interface BufferedReaderProcessor {
		String process(BufferedReader b) throws IOException;
	}

	// 9.3 (람다 테스팅)
	// 9.3.1 (보이는 람다 표현식의 동작 테스팅 - 람다는 익명(익명함수)이므로 테스트 코드 이름 호출 X)
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

		public int getY() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

	}

	// thenComparing - 첫 번째 비교자가 같으면 두 번째 객체로 비교
	public final static Comparator<Point> compareByXAndThenY = comparing(Point::getX).thenComparing(Point::getY);

	@Test
	public void testComparingTwoPoints() throws Exception {
		Point p1 = new Point(10, 15);
		Point p2 = new Point(10, 20);
		int result = doSomething.compareByXAndThenY.compare(p1, p2);
		assertTrue(result < 0);
	}

	// 9.3.2 (람다를 사용하는 메서드의 동작에 집중)
	// 람다 목표: 정해진 동작을 다른 메서드에서 사용 할 수 있도록 하나의 조각으로 캡슐화
	// 람다 표현식을 사용하는 메서드 동작 → 테스트 → 람다 공개X + 표현식 검증 O
	public static List<Point> moveAllPointsRightBy(List<Point> points, int x) {
		return points.stream().map(p -> new Point((int) p.getX() + x, (int) p.getY())).collect(toList());
	}

	// Point의 equals를 적절하게 다시 구현해야함
	@Test
	public void testMoveAllPointsRightBy() throws Exception {
		List<Point> points = Arrays.asList(new Point(5, 5), new Point(10, 5));
		List<Point> expectedPoints = Arrays.asList(new Point(15, 5), new Point(20, 5));
		List<Point> newPoints = doSomething.moveAllPointsRightBy(points, 10);
		assertEquals(expectedPoints, newPoints);
	}
}
