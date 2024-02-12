package Chap09;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// 9.2.5 (팩토리 - 인스턴스화 로직을 클라이언트에 노출하지 않고 객체를 만들 때 사용)
public class FactoryMain {
	public static void main(String[] args) {
		Product p1 = ProductFactory.createProduct("loan");
		System.out.printf("p1: %s%n", p1.getClass().getSimpleName());

		Product p2 = ProductFactory.createProductLambda("loan");
		System.out.printf("p3: %s%n", p2.getClass().getSimpleName());

		// 람다 사용
		Supplier<Product> loanSupplier = Loan::new;
		Product loan = loanSupplier.get();
		System.out.printf("name: %s%n", loan.getClass().getSimpleName());
	}

	static private class ProductFactory {
		public static Product createProduct(String name) {
			switch (name) {
			case "loan":
				return new Loan();
			case "stock":
				return new Stock();
			case "bond":
				return new Bond();
			default:
				throw new RuntimeException("No such product " + name);
			}
		}

		public static Product createProductLambda(String name) {
			Supplier<Product> p = map.get(name);
			if (p != null) {
				return p.get();
			}
			throw new RuntimeException("No such product " + name);
		}
	}

	static private interface Product {
	}

	static private class Loan implements Product {
	}

	static private class Stock implements Product {
	}

	static private class Bond implements Product {
	}

	final static private Map<String, Supplier<Product>> map = new HashMap<>();
	static {
		map.put("loan", Loan::new);
		map.put("stock", Stock::new);
		map.put("bond", Bond::new);
	}

	// 번외: 생성자로 여러 인수가 필요할 때(세 인수가 필요하다고 가정) → TriFunction과 같은 새로운 함수형 인터페이스 제작 필요
	public interface TriFunction<T, U, V, R> {
		R apply(T t, U u, V v);
	}

	Map<String, TriFunction<Integer, Integer, String, Product>> map2 = new HashMap<>();
}
