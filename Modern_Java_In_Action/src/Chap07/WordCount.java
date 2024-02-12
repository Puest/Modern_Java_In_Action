package Chap07;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

//함수형으로 단어 수를 세는 메서드
public class WordCount {
	public static final String SENTENCE = " Nel   mezzo del cammin  di nostra  vita "
			+ "mi  ritrovai in una  selva oscura" + " che la  dritta via era   smarrita ";

	public static void main(String[] args) {
		System.out.println("Found " + countWordsIteratively(SENTENCE) + "words");
		System.out.println("Found " + countWords(SENTENCE) + "words");
	}

	// 반복형으로 단어 수를 세는 메서드
	public static int countWordsIteratively(String s) {
		int counter = 0;
		boolean lastSpace = true;
		for (char c : s.toCharArray()) {
			if (Character.isWhitespace(c)) { // 문자가 공백 문자인지 판단
				lastSpace = true;
			} else {
				if (lastSpace) {
					counter++; // 이전까지의 문자를 단어로 간주하여 단어 수 체크
				}
				lastSpace = Character.isWhitespace(c);
			}
		}
		return counter;
	}

	public static int countWords(String s) {
//		Stream<Character> stream = IntStream.range(0, s.length()).mapToObj(SENTENCE::charAt).parallel();
		Spliterator<Character> spliterator = new WordCounterSpliterator(s);
		Stream<Character> stream = StreamSupport.stream(spliterator, true);

		return countWords(stream);
	}

	private static int countWords(Stream<Character> stream) {
		WordCounter wordCounter = stream.reduce(new WordCounter(0, true), WordCounter::accumulate,
				WordCounter::combine);
		return wordCounter.getCount();
	}

	private static class WordCounter {
		private final int counter;
		private final boolean lastSpace;

		public WordCounter(int counter, boolean lastSpace) {
			this.counter = counter;
			this.lastSpace = lastSpace;
		}

		public WordCounter accumulate(Character c) {
			if (Character.isWhitespace(c)) { // 문자열의 문자를 하나씩 탐색
				return lastSpace ? this : new WordCounter(counter, true);
			} else {
				// 공백 문자를 만났을 경우 → 지금까지 탐색한 문자를 단어로 간주
				return lastSpace ? new WordCounter(counter + 1, false) : this;
			}
		}

		public WordCounter combine(WordCounter wordCounter) {
			return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
		}

		public int getCount() {
			return counter;
		}
	}

	private static class WordCounterSpliterator implements Spliterator<Character> {
		private final String string;
		private int currentChar = 0;

		public WordCounterSpliterator(String string) {
			this.string = string;
		}

		/**
		 * 문자열에서 현재 인덱스에 해당하는 문자를 Consumer에 제공한 다음, 인데스를 증가시킨다.
		 *
		 * @param action 소비한 문자를 전달
		 * @return 소비할 문자가 남아있으면 true를 반환 (반복해야 할 문자가 남아있음을 의미)
		 */
		@Override
		public boolean tryAdvance(Consumer<? super Character> action) {
			action.accept(string.charAt(currentChar++));
			return currentChar < string.length();
		}

		/**
		 * 반복될 자료구조를 분할하는 로직을 포함한다. 분할이 필요한 상황에서는 파싱해야 할 문자열 청크의 중간 위치를 기준으로 분할하도록 지시한다.
		 *
		 * @return 남은 문자 수가 한계값 이하면 null 반환 -> 분할을 중지하도록 지시
		 */
		@Override
		public Spliterator<Character> trySplit() {
			int currentSize = string.length();
			if (currentSize < 10) {
				return null; // 파싱할 문자열이 순차 처리할 수 있을 만큼 충분히 작아졌음을 알림
			}

			// 1. 파싱할 문자열의 중간을 분할 위치로 설정
			for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
				// 2. 다음 공백이 나올 때까지 분할 위치를 뒤로 이동시킴
				if (Character.isWhitespace(string.charAt(splitPos))) {
					// 3. 처음부터 분할위치까지 문자열을 파싱할 새로운 WordCounterSpliterator를 생성
					Spliterator<Character> spliterator = new WordCounterSpliterator(
							string.substring(currentSize, splitPos));
					// 4. 이 WordCounterSpliterator의 시작 위치를 분할 위치로 설정
					currentChar = splitPos;
					// 5. 공백을 찾았고 문자열을 분리했으므로 루프를 종료
					return spliterator;
				}
			}
			return null;
		}

		/**
		 * @return 탐색해야 할 요소의 개수
		 */
		@Override
		public long estimateSize() {
			return string.length() - currentChar;
		}

		/**
		 * @return 특성들
		 */
		@Override
		public int characteristics() {
			return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
		}
	}
}
