package Chap11;

import java.util.Optional;

public class Person {
	private Car car;

	public Car getCar() {
		return car;
	}

	// 직렬화 모델 필요시, Optional로 값을 반환받을 수 있는 메서드를 추가하는 방식을 권장
	public Optional<Car> getCarAsOptional() {
		return Optional.ofNullable(car);
	}
}
