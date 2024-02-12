package Chap06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

//6.5.1 (collector 인터페이스 메서드)
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