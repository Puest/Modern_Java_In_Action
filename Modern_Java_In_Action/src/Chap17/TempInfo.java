package Chap17;

import java.util.Random;

// 원격 온도계 (0~99 사이의 화씨 온도를 만들어 연속으로 보고)
public class TempInfo {
	public static final Random random = new Random();

	private final String town;
	private final int temp;

	public TempInfo(String town, int temp) {
		this.town = town;
		this.temp = temp;
	}

	public static TempInfo fetch(String town) {
		if (random.nextInt(10) == 0) {
			throw new RuntimeException("Error!"); // 10% 확률로 실패
		}

		return new TempInfo(town, random.nextInt(10));
	}

	@Override
	public String toString() {
		return town + " : " + temp;
	}

	public int getTemp() {
		return temp;
	}

	public String getTown() {
		return town;
	}
}
