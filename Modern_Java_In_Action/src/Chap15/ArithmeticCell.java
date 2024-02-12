package Chap15;

// 15.5.1(C3 = C1 + C2)
public class ArithmeticCell extends SimpleCell {
	private int left;
	private int right;

	public ArithmeticCell(String name) {
		super(name);
	}

	public void setLeft(int left) {
		this.left = left;
		onNext(left + this.right); // 셀 값을 갱신하고 구독자에게 알림
	}

	public void setRight(int right) {
		this.right = right;
		onNext(right + this.left); // 셀 값을 갱신하고 구독자에게 알림
	}

	public static void main(String[] args) {
		ArithmeticCell c3 = new ArithmeticCell("c3");

		SimpleCell c2 = new SimpleCell("c2");
		SimpleCell c1 = new SimpleCell("c1");

		c1.subscribe(c3::setLeft);
		c2.subscribe(c3::setRight);

		c1.onNext(10); // c1의 값을 10으로 갱신
		c2.onNext(20); // c2의 값을 20으로 갱신
		c1.onNext(15); // c1의 값을 15으로 갱신
	}

}
