package Chap11;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OptionalMain {
	public static void main(String[] args) {

	}

	// 11.1.2 (null 때문에 발생하는 문제 - 차를 소유 X → 호출 → NullPointerException 발생)
	public String getCarInsuranceName(Person person) {
		return person.getCar().getInsurance().getName();
	}

	// 11.1.3(보수적인 자세로 NullPointerException 줄이기 - 예기치 않은 NPE를 피하기위해 사용)
	// 모든 변수 null 체크 → 코드가 지저분, 반복 패턴 多 => Deep Doubt
	public String getCarInsuranceNameV2(Person person) {
		if (person != null) {
			Car car = person.getCar();
			if (car != null) {
				Insurance insurance = car.getInsurance();
				if (insurance != null) {
					return insurance.getName();
				}
			}
		}
		return "Unknown";
	}

	// null인 경우 반환 → return하는 통로 多 → 유지보수 어려움, 다른 사람이 null 사실을 까먹는 경우 NPE 발생
	public String getCarInsuranceNameV3(Person person) {
		if (person == null) {
			return "Unknown";
		}
		Car car = person.getCar();
		if (car == null) {
			return "Unknown";
		}

		Insurance insurance = car.getInsurance();
		if (insurance == null) {
			return "Unknown";
		}

		return insurance.getName();
	}

	// 11.2 (Optional 클래스 - 차를 소유 X → 호출(Optional) → Optional.empty)
//	public String getCarInsuranceName(PersonO person) {
//		return person.getCar().getInsurance().getName();
//	}

	// 11.3.1 (Optional 객체 만들기)
	// 빈 Optional 제작
	Optional<CarO> optCar = Optional.empty();

	// null이 아닌 값으로 Optional 제작(car가 null이면 NPE 발생)
//	Optional<CarO> optCar2 = Optional.of(car);

	// null 값으로 Optional 제작(ofNullable로 null값을 저장 - car가 null이면 빈 Optional 객체 반환)
//	Optional<CarO> optCar3 = Optional.ofNullable(car);

	// 11.3.2 (Map으로 Optional값 추출, 변환)
	// 보험회사 이름을 추출하는 코드
//	String name = null;if(insurance!=null)
//	{
//		name = insurance.getName();
//	}

	// Optional 사용 시
//	Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
//	Optional<String> name = optInsurance.map(Insurance::getName);

	// 11.3.3 (flatMap으로 Optional 객체)
	// 컴파일 실패 → 이차원 Optional 형식(Optional<Optional<?>>) 때문
//		Optional<PersonO> optPerson = Optional.of(person);
//		Optional<String> name = 
//				optPerson.map(PersonO::getCar)	//Optional<Optional<Car>>
//				.map(CarO::getInsurance)			//Optional<Optional<Insurance>>
//				.map(Insurance::getName);

	// 보완: faltMap으로 Optional 객체 연결 → Optional 일차원 평준화
	public String getCarInsuranceNameV4(PersonO person) {
		Optional<PersonO> optionalPerson = Optional.of(person);
		String name = optionalPerson.flatMap(PersonO::getCar).flatMap(CarO::getInsurance).map(Insurance::getName)
				.orElse("UNKNOWN"); // 결과 Optional이 비어있으면 기본값 사용
		return name;
	}

	// 11.3.4 (Optional 스트림 조작 - 자바9에서는 Optional 포함하는 스트림을 쉽게 처리할 수 있도록 메서드 제공)
	public Set<String> getCarInsuranceNameV5(List<PersonO> persons) {
		// 세 번의 변환 과정(map)을 거친 결과 Stream<Optional>를 얻는데 결과가 비어있을 수 있음 → 차X, 보험 X
		return persons.stream().map(PersonO::getCar)// 사람 목록을 각 사람이 보유한 자동차의 Optional<Car> 스트림으로 변환
				.map(optCar -> optCar.flatMap(CarO::getInsurance)) // Optional<Car>를 Optional<Insurance>로 변환
				.map(optIns -> optIns.map(Insurance::getName)) // Optional<Insurance>를 해당 이름의 Optional<String>으로 변환
				.flatMap(Optional::stream) // Stream<Optional<String>>을 현재 이름을 포함하는 Stream<String>으로 변환
				.collect(toSet());

		// 완전한 결과를 얻기 위해 빈 Optional을 제거 후 값을 언랩
//		Stream<Optional<String>> optStream = persons.stream().map(PersonO::getCar) // Optional<Car> 스트림으로 변환
//				.map(optCar -> optCar.flatMap(CarO::getInsurance)) // Optional<getInsurance> 스트림으로 변환
//				.map(optIns -> optIns.flatMap(Insurance::getName)); // Optional<String> 스트림으로 변환

//		Set<String> collect = optStream.filter(Optional::isPresent).map(Optional::get).collect(toSet());
	}

	// 11.3.6 (두 Optional 합치기)
	// Person과 Car 정보를 이용해 가장 저렴한 보험료를 제공하는 보험회사를 찾는 로직 구현
	public Insurance findCheapestInsurance(PersonO person, CarO car) {
		// 다양한 보험회사가 제공하는 서비스 조회
		// 모든 결과 데이터 비교
		Insurance cheapestCompany = new Insurance();
		return cheapestCompany;
	}

	// 두 Optional을 인수로 받아서 Optional를 반환하는 null 안전 버전(nullsafe version)의 메서드를 구현 →
	// null 검사코드와 다를 것이 크게 없음.
	public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<PersonO> person, Optional<CarO> car) {
		if (person.isPresent() && car.isPresent()) {
			return Optional.of(findCheapestInsurance(person.get(), car.get()));
		} else {
			return Optional.empty();
		}
	}

	// map과 flatMap 메서드를 통해 재구현
	public Optional<Insurance> nullSafeFindCheapestInsurance2(Optional<PersonO> person, Optional<CarO> car) {
		return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
	}

	// 11.3.7 (필터로 특정값 거르기)
	// 보험회사 이름이 ‘CambridgeInsurance’인지 확인하는 코드
//	Optional<Insurance> optInsurance = ....;
//	if(optInsurance != null && "CambridgeInsurance".equals(insurance.getName())){}	//객체 프로퍼티 확인(일반)
//	optInsurance.filter(insurance -> "CambridgeInsurance".equals(insurance.getName()))	//Optional객체 + filter메서드
//			.ifPresent(x -> System.out.println("ok"));

	// 인수 person이 minAge 이상의 나이일 때만 보험회사 이름을 반환하는 코드
	public String getCarInsuranceName(Optional<PersonO> person, int minAge) {
		return person.filter(p -> p.getAge() >= minAge).flatMap(PersonO::getCar).flatMap(CarO::getInsurance)
				.map(Insurance::getName).orElse("Unknown");
	}

	// 11.4 (Optional 사용)
	// 11.4.1 (잠재적 null인 대상을 Optional로 감싸기)
//	Object value = map.get("key");	//map에 해당 값이 없으면 null 반환

	// 해결방법 (1.if-then-else 추가, 2.Optional.ofNullable사용)
//	Optional<Object> value = Optional.ofNullable(map.get("key"));

	// 11.4.2 (예외와 Optional 클래스 - Integer.parseInt(String))
	public static Optional<Integer> stringToInt(String s) {
		try {
			return Optional.of(Integer.parseInt(s)); // 정수로 변환된 값을 포함하는 Optional 반환
		} catch (NumberFormatException e) {
			return Optional.empty(); // 빈 Optional 반환
		}
	}
}
