package Chap16;

import static modernjavainaction.chap16.Util.delay;
import static modernjavainaction.chap16.Util.format;

import java.util.Random;

public class DiscntShop {
	private final String name;
	private final Random random;

	public DiscntShop(String name) {
		this.name = name;
		random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
	}

	public String getPrice(String product) {
		double price = calculatePrice(product);
		Discount.Code value = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)];
		return String.format("$s:%.f:$s", name, price, value);
	}

	public double calculatePrice(String product) {
		delay();
		return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
	}

}
