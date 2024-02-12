package Chap19;

// 19.2 (영속 자료구조)
// 19.2.2 (트리 예제)
public class Tree {
	private String key;
	private int val;
	private Tree left, right;

	public Tree(String k, int v, Tree l, Tree r) {
		key = k;
		val = v;
		left = l;
		right = r;
	}

	public static void main(String[] args) {
		Tree t = new Tree("Mary", 22,
				new Tree("Emily", 20, new Tree("Alan", 50, null, null), new Tree("Georgie", 23, null, null)),
				new Tree("Tian", 29, new Tree("Raoul", 23, null, null), null));

		// 발견 = 23
		System.out.printf("Raoul: %d%n", TreeProcessor.lookup("Raoul", -1, t));
		// 발견되지 않음 = -1
		System.out.printf("Jeff: %d%n", TreeProcessor.lookup("Jeff", -1, t));

		Tree f = fupdate("Jeff", 80, t);
		// 발견 = 80
		System.out.printf("Jeff: %d%n", TreeProcessor.lookup("Jeff", -1, f));

		Tree u = update("Jim", 40, t);
		// fupdate로 t가 바뀌지 않았으므로 Jeff는 발견되지 않음 = -1
		System.out.printf("Jeff: %d%n", TreeProcessor.lookup("Jeff", -1, u));
		// 발견 = 40
		System.out.printf("Jim: %d%n", TreeProcessor.lookup("Jim", -1, u));

		Tree f2 = fupdate("Jeff", 80, t);
		// 발견 = 80
		System.out.printf("Jeff: %d%n", TreeProcessor.lookup("Jeff", -1, f2));
		// t로 만든 f2는 위 update()에서 갱신되므로 Jim은 여전히 존재함 = 40
		System.out.printf("Jim: %d%n", TreeProcessor.lookup("Jim", -1, f2));
	}

	// 트리의 다른 작업을 처리하는 기타 메서드
	static class TreeProcessor {
		public static int lookup(String k, int defaultval, Tree t) {
			if (t == null)
				return defaultval;
			if (k.equals(t.key))
				return t.val;
			return lookup(k, defaultval, k.compareTo(t.key) < 0 ? t.left : t.right);
		}
	}

	// 아래 두 가지 update 버전 모두 기존 트리를 변경하고 트리에 저장된 맵의 모든 사용자가 변경에 영향을 받는다.
	public static void update2(String k, int newval, Tree t) {
		if (t == null) {
			/* 새로운 노드 추가 */
		} else if (k.equals(t.key))
			t.val = newval;
		else
			update2(k, newval, k.compareTo(t.key) < 0 ? t.left : t.right);
	}

	private static Tree update(String key, int newval, Tree t) {
		if (t == null) {
			t = new Tree(key, newval, null, null);
		} else if (key.equals(t.key))
			t.val = newval;
		else if (key.compareTo(t.key) < 0)
			t.left = update(key, newval, t.left);
		else
			t.right = update(key, newval, t.right);

		return t;
	}

	// if-then-else 문으로 코드 구현, fupdate(new Tree) 호출로 새로운 트리 노드 제작 → 기존 자료구조 영향 X
	public static Tree fupdate(String k, int newval, Tree t) {
		return (t == null) ? new Tree(k, newval, null, null)
				: k.equals(t.key) ? new Tree(k, newval, t.left, t.right)
						: k.compareTo(t.key) < 0 ? new Tree(t.key, t.val, fupdate(k, newval, t.left), t.right)
								: new Tree(t.key, t.val, t.left, fupdate(k, newval, t.right));
	}
}
