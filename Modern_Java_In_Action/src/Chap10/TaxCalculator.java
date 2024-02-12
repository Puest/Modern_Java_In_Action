package Chap10;

import static Chap10.MixedBuilder.buy;
import static Chap10.MixedBuilder.forCustomer;
import static Chap10.MixedBuilder.sell;

import java.util.function.DoubleUnaryOperator;

import Chap10.model.Order;
import Chap10.model.Tax;

// 10.3.5 (DSL에 메서드 참조 사용)
public class TaxCalculator {
	public static void main(String[] args) {
		Order order = forCustomer("BigBank", buy(t -> t.quantity(80).stock("IBM").on("NYSE").at(125.00)),
				sell(t -> t.quantity(50).stock("GOOGLE").on("NASDAQ").at(125.00)));

		double value = TaxCalculator.calculate(order, true, false, true);
		System.out.printf("Boolean references: %.2f%n", value);

		value = new TaxCalculator().withTaxRegional().withTaxSurcharge().calculateF(order);
		System.out.printf("Method chaining: %.2f%n", value);

		value = new TaxCalculator().with(Tax::regional).with(Tax::surcharge).calculateF(order);
		System.out.printf("Method references: %.2f%n", value);
	}

	private boolean useRegional;
	private boolean useGeneral;
	private boolean useSurcharge;

	public TaxCalculator withTaxRegional() {
		useRegional = true;
		return this;
	}

	public TaxCalculator withTaxGeneral() {
		useGeneral = true;
		return this;
	}

	public TaxCalculator withTaxSurcharge() {
		useSurcharge = true;
		return this;
	}

	public double calculate(Order order) {
		return calculate(order, useRegional, useGeneral, useSurcharge);
	}

	public static double calculate(Order order, boolean useRegional, boolean useGeneral, boolean useSurcharge) {
		double value = order.getValue();
		if (useRegional) {
			value = Tax.regional(value);
		}
		if (useGeneral) {
			value = Tax.general(value);
		}
		if (useSurcharge) {
			value = Tax.surcharge(value);
		}
		return value;
	}

	// 주문 값에 적용된 모든 세금을 계산하는 함수
	public DoubleUnaryOperator taxFunction = d -> d;

	public TaxCalculator with(DoubleUnaryOperator f) {
		// 새로운 세금 계산 함수를 얻어서 현재 함수와 합침
		taxFunction = taxFunction.andThen(f);
		return this;
	}

	public double calculateF(Order order) {
		// 전달되어있는 함수를 계산한다.
		return taxFunction.applyAsDouble(order.getValue());
	}

}
