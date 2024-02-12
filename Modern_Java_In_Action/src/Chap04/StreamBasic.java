package Chap04;

import static Chap04.Dish.menu;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StreamBasic {
	public static void main(String[] args) {
		// 4.1 - 4.3 (스트림과 컬렉션)
		// 자바 7(컬렉션 → 외부 반복)
		// 저칼로리 요리들
		List<Dish> lowCaloricDishes = new ArrayList<>(); // 가비지(garbage) 변수 : 컨테이너 역할만 하는 중간 변수
		for (Dish dish : menu) { // 누적자로 요소 필터링(메뉴 리스트를 명시적으로 순차 반복함)
			if (dish.getCalories() < 400) { // 칼로리가 400 미만인 음식을 list에 저장(menu 순서대로)
				lowCaloricDishes.add(dish);
			}
		}

		// 익명 클래스로 요리들을 칼로리 기준으로 정렬
		Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
			public int compare(Dish dish1, Dish dish2) {
				return Integer.compare(dish1.getCalories(), dish2.getCalories()); // list 정렬(작은 순서대로)
			}
		});

		List<String> lowCaloricDishesName = new ArrayList<>();
		for (Dish dish : lowCaloricDishes) {
			lowCaloricDishesName.add(dish.getName()); // 정렬된 리스트를 처리하면서 요리 이름 선택
		}

		// 자바 8 (스트림 → 내부 반복),(stream()을 parallelstream()으로 병렬 처리가 가능)
		List<String> lowCaloricDishesName2 = menu.stream().filter(d -> d.getCalories() < 400) // 400 칼로리 이하의 요리 선택
				.sorted(comparing(Dish::getCalories)) // 칼로리 요리 정렬
				.map(Dish::getName) // 요리명 추출(map 메서드를 getName 메서드로 파라미터화)
				.collect(toList()); // 추출된 요리명을 리스트에 저장(파이프라인을 실행, 반복자 필요 X)
		System.out.println(lowCaloricDishesName2);

		// 4.4 (스트림 연산[중간, 최종])
		List<String> names = menu.stream().filter(dish -> { // 필터링한 요리명 출력
			System.out.println("filtering: " + dish.getName());
			return dish.getCalories() > 300;
		}).map(dish -> { // 추출한 요리명 출력
			System.out.println("mapping: " + dish.getName());
			return dish.getName();
		}).limit(3).collect(toList());
		System.out.println(names);
	}
}
