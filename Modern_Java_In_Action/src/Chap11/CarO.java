package Chap11;

import java.util.Optional;

public class CarO {
	// 자동차가 보험에 가입되어 있을 수도, 가입되어 있지 않았을 수도 있음
	public Optional<Insurance> insurance;

	public Optional<Insurance> getInsurance() {
		return insurance;
	}
}
