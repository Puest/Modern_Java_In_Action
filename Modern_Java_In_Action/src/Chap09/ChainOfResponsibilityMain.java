package Chap09;

import java.util.function.Function;
import java.util.function.UnaryOperator;

// 9.2.4 (의무 체인)
public class ChainOfResponsibilityMain {
	public static void main(String[] args) {
		ProcessingObject<String> p1 = new HeaderTextProcessing();
		ProcessingObject<String> p2 = new SpellCheckerProcessing();
		// 두 객체 연결
		p1.setSuccessor(p2);
		// p1.handle(r) → r(r1) → p2.handle(r) → r(r2)
		String result = p1.handle("Aren't labdas really sexy?!!");

		// 람다 - 함수 조합 이용 → 작업 처리 객체를 Function 즉, UnaryOperator형식의 인스턴스로 표현 O
		// andThen 메서드로 함수를 조합해서 체인을 만들 수 있음
		UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan: " + text;
		UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda", "lambda");
		Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
		String result2 = pipeline.apply("Aren't labdas really sexy?!!");

	}

	// 템플릿 메서드 이용 - 전체적인 작업의 개요 → handle메서드가 기술하고, 구체적인 알고리즘은 → 객체가 handleWork를 구현하여
	// 책임진다
	private static abstract class ProcessingObject<T> {
		protected ProcessingObject<T> successor;

		public void setSuccessor(ProcessingObject<T> successor) {
			this.successor = successor;
		}

		public T handle(T input) {
			T r = handleWork(input);
			// 구성하고 있는 successor 객체에 결과를 전달하며 호출
			if (successor != null) {
				return successor.handle(r);
			}
			return r;
		}

		abstract protected T handleWork(T input);
	}

	private static class HeaderTextProcessing extends ProcessingObject<String> {
		@Override
		public String handleWork(String text) {
			return "From Raoul, Mario and Alan: " + text;
		}
	}

	private static class SpellCheckerProcessing extends ProcessingObject<String> {
		@Override
		protected String handleWork(String text) {
			return text.replaceAll("labda", "lambda");
		}
	}
}
