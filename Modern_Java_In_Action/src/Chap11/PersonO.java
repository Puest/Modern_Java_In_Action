package Chap11;

import java.util.Optional;

public class PersonO {
	// 사람은 차를 소유했을 수도, 소유하지 않았을 수도 있음
	private Optional<CarO> car;
	private int age;

	public Optional<CarO> getCar() {
		return car;
	}

	public int getAge() {
		return age;
	}
}
