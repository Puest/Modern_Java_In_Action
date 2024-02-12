package Chap11;

import static org.junit.Assert.assertEquals;

import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class ReadPositiveIntParam {
	@Test
	public void testMap() {
		Properties props = new Properties();
		props.setProperty("a", "5");
		props.setProperty("b", "true");
		props.setProperty("c", "-3");

		assertEquals(5, readDurationImperative(props, "a"));
		assertEquals(0, readDurationImperative(props, "b"));
		assertEquals(0, readDurationImperative(props, "c"));
		assertEquals(0, readDurationImperative(props, "d"));

		assertEquals(5, readDurationWithOptional(props, "a"));
		assertEquals(0, readDurationWithOptional(props, "b"));
		assertEquals(0, readDurationWithOptional(props, "c"));
		assertEquals(0, readDurationWithOptional(props, "d"));
	}

	// Optional 없이 검증을 구현 (try/catch 블록과 if문이 중첩되면서 코드 복잡 + 가독성 Bad)
	public static int readDurationImperative(Properties props, String name) {
		String value = props.getProperty(name);
		if (value != null) { // Properties에 name에 해당하는 값이 있는지 확인
			try {
				int i = Integer.parseInt(value); // 값을 숫자로 변환
				if (i > 0) { // 양의 정수일 경우 값 반환
					return i;
				}
			} catch (NumberFormatException nfe) {
			}
		}
		return 0; // 조건에 없다면 0 반환
	}

	// Optional을 통한 검증 구현
	public static int readDurationWithOptional(Properties props, String name) {
		return Optional.ofNullable(props.getProperty(name)).flatMap(ReadPositiveIntParam::stringToInt) // 정수로 변환실패 시 0
																										// 반환하는 유틸리티 메소드
				.filter(i -> i > 0).orElse(0);
	}

	public static Optional<Integer> stringToInt(String s) {
		try {
			return Optional.of(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
}
