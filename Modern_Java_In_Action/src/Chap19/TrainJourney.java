package Chap19;

// 19.2 (영속 자료구조)
// 19.2.1 (파괴적인 갱신과 함수형)
public class TrainJourney {
	// A에서 B까지 기차여행을 의미하는 가변 TrainJourney 클래스
	// 두 개의 TrainJourney 객체를 연결해서 하나의 여행을 만들고자 한다.
	public static void main(String[] args) {
		TrainJourney t1 = new TrainJourney(40, new TrainJourney(30, null));
	}

	public int price;
	public TrainJourney onward; // 이어지는 여정

	public TrainJourney(int p, TrainJourney t) {
		price = p;
		onward = t;
	}

	@Override
	public String toString() {
		return String.format("TrainJourney[%d] -> %s", price, onward);
	}

	// 기차여행을 연결(link)
	// firstJourney가 secondJourney를 포함하면서 파괴적인 갱신(firstJourney의 변경)이 일어남
	public static TrainJourney link(TrainJourney a, TrainJourney b) {
		if (a == null)
			return b;
		TrainJourney t = a;
		while (t.onward != null) {
			t = t.onward;
		}
		t.onward = b;
		return a;
	}

	// a가 n 요소(새로운 노드)의 시퀀스고 b가 m 요소(TrainJourney b과 공유되는 요소의 시퀀스라면,
	// n+m 요소의 시퀀스를 반환
	public static TrainJourney append(TrainJourney a, TrainJourney b) {
		return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
	}
}
