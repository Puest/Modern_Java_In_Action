package Chap09;

// 9.2 (람다로 객체지향 디자인 패턴 리팩터링)
// 9.2.1 (전략(strategy) - 한 유형의 알고리즘을 보유한 상태에서 런타임에 적절한 알고리즘을 선택하는 기법)
public class StrategyMain {
	public static void main(String[] args) {
		Validator numericValidator = new Validator(new IsNumeric());
		Validator lowerCaseValidator = new Validator(new IsAllLowerCase());

		// 람다 사용 (람다를 직접 전달)
		Validator lowerCaseValidator2 = new Validator((String s) -> s.matches("[a-z]+"));
		Validator numericValidator2 = new Validator((String s) -> s.matches("\\d+"));
	}

	// 9.2.1
	// 전략 인터페이스, 함수형 인터페이스이다.
	public interface ValidationStrategy {
		boolean execute(String s);
	}

	// 전략을 구현하는 구현 객체
	static private class IsAllLowerCase implements ValidationStrategy {
		@Override
		public boolean execute(String s) {
			// matches - 문자열의 처음부터 정규식과 매치되는지 조사
			return s.matches("[a-z]+");
		}
	}

	static private class IsNumeric implements ValidationStrategy {
		@Override
		public boolean execute(String s) {
			return s.matches("\\d+");
		}
	}

	// 클라이언트 전략을 생성자로 부터 받아와 이용한다.
	static private class Validator {
		private final ValidationStrategy strategy;

		public Validator(ValidationStrategy v) {
			this.strategy = v;
		}

		public boolean validate(String s) {
			return strategy.execute(s);
		}
	}
}
