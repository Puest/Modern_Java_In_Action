package Chap12;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeExamples {
	public static void main(String[] args) {
		useLocalDate();
		useTemporalAdjuster();
		useDateFormatter();
		useZone();
	}

	// LocalDate와 LocalTime의 사용
	private static void useLocalDate() {
		// LocalDate - 시간을 제외한 날짜를 표현
		LocalDate date = LocalDate.of(2020, 12, 18);
		int year = date.getYear();
		Month month = date.getMonth(); // Enum으로 날짜 리턴
		int day = date.getDayOfMonth();
		LocalDate now = LocalDate.now(); // 팩토리 메서드 now로 현재 날짜 정보 get
		System.out.println(now);

		// get(TemporalField field) - TemporalField는 시간 관련 객체에서 어떤 필드의 값에 접근할지 정의
		int y = date.get(ChronoField.YEAR);
		int m = date.get(ChronoField.MONTH_OF_YEAR);
		int d = date.get(ChronoField.DAY_OF_MONTH);
		System.out.println(y + " " + m + " " + d);

		// LocalTime - 시간을 표현
		LocalTime time = LocalTime.of(13, 45, 20);
		int hour = time.getHour();
		int minute = time.getMinute();
		int second = time.getSecond();
		System.out.println(hour + " " + minute + " " + second);

		// parse - 날짜(-), 시간(:)을 문자열로 만들 수 있음
		LocalDate date2 = LocalDate.parse("2020-12-20");
		LocalTime time2 = LocalTime.parse("13:45:30");
		System.out.println(date2 + "\n" + time2);

		// 날짜와 시간 조합 (LocalDateTime = LocalDate + LocalTime → 날짜, 시간 모두 표현 O)
		LocalDateTime ldt1 = LocalDateTime.of(2018, Month.OCTOBER, 11, 18, 30, 25); // 년,월,일,시,분,초
		LocalDateTime ldt2 = LocalDateTime.of(date, time); // 2020-12-22T13:45:20
		LocalDateTime ldt3 = date.atTime(time); // 2020-12-22T13:45:20
		LocalDateTime ldt4 = time.atDate(date); // 2020-12-22T13:45:20
		System.out.println(ldt1 + "\n" + ldt2);

		// Instant 클래스: 기계의 날짜와 시간 - 기계적인 관점에서 시간을 표현
		Instant instant = Instant.ofEpochSecond(3);
		Instant instant2 = Instant.ofEpochSecond(4, 0); // 두 번째 인수 → 나노초 단위까지 시간 보장
		Instant Inow = Instant.now(); // now() - 사람이 확인할 수 있도록 시간 표현(읽을 수 있는 시간정보는 제공 X)
		// ↑ 모두 Temporal 인터페이스를 구현 → 특정 시간을 모델링하는 객체의 값을 어떻게 읽고 조작할지 정의

		// Duration과 Period 정의 - 자신의 인스턴스를 만들 수 있도록 다양한 팩토리 메서드를 제공
		// Duration.between - 두 시간 객체 사이의 지속시간을 만듦
		Duration d1 = Duration.between(time, time2);
		Duration d3 = Duration.between(instant, instant2);
		System.out.println("Seconds:" + d1.getSeconds());
		System.out.println("Seconds:" + d3.getSeconds());

		// Period.between - 두 LocalDate의 차이를 확인 O
		Period tenDays = Period.between(date, date2);
		System.out.println("Days:" + tenDays.getDays());

		// of() - Period, Duration 인스턴스를 생성하여 반환
		Duration threeMinutes = Duration.of(3, ChronoUnit.MINUTES);// 3일차이
		System.out.println(threeMinutes);

		Period threeDays = Period.ofDays(3); // 3일 차이
		System.out.println(threeDays);

		Period threeWeeks = Period.ofWeeks(3); // 3주 차이
		System.out.println(threeWeeks);

		JapaneseDate japaneseDate = JapaneseDate.from(date);
		System.out.println(japaneseDate);

	}

	private static void useTemporalAdjuster() {
		// 날짜 조정, 파싱, 포메팅
		// withAttribte - LocalDate를 바꿈
		// 절대적인 방식으로 LocalDate 속성 바꾸기
		LocalDate date1 = LocalDate.of(2017, 9, 21); // 2017-09-21
		LocalDate date2 = date1.withYear(2023); // 2023-09-21
		LocalDate date3 = date2.withDayOfMonth(10); // 2017-09-10
		// TemporalFiled를 갖는 메서드는 범용적으로 날짜를 변경 O
		LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 2); // 2023-2-10
		System.out.println(date1 + "\n" + date2 + "\n" + date3 + "\n" + date4);

		// 선언형으로 LocalDate를 사용
		LocalDate date5 = LocalDate.of(2017, 9, 21); // 2017-09-21
		LocalDate date6 = date5.plusWeeks(1); // 2017-09-28
		LocalDate date7 = date6.minusYears(6); // 2011-09-28
		LocalDate date8 = date7.plus(6, ChronoUnit.MONTHS); // 2012-03-28

		// TemporalAdjusters 사용 - 복잡한 날짜 조정 기능을 지원(날짜와 시간 API는 다양한 상황에서도 사용하도록 제공)
		// 필요한 기능이 존재 X → 커스텀 TemporalAdjusters구현하여 사용
		LocalDate dat = LocalDate.of(2014, 3, 18);
		System.out.println(dat);
		dat = dat.with(nextOrSame(DayOfWeek.SUNDAY)); // 현재 날짜 이후로 지정한 요일이 처음으로 나타나는 날짜
		System.out.println(dat);
		dat = dat.with(lastDayOfMonth()); // 현재 달의 마지막 날짜 반환
		System.out.println(dat);

		dat = dat.with(new NextWorkingDay());
		System.out.println(dat);
		dat = dat.with(nextOrSame(DayOfWeek.FRIDAY));
		System.out.println(dat);
		dat = dat.with(new NextWorkingDay());
		System.out.println(dat);
	}

	private static class NextWorkingDay implements TemporalAdjuster {
		// Working Day 만 구하는 커스텀 TemporalAdjuster
		@Override
		public Temporal adjustInto(Temporal temporal) {
			DayOfWeek today = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK)); // 현재 날짜 읽기
			int dayToAdd = 1;
			if (today == DayOfWeek.FRIDAY) {
				dayToAdd = 3;
			} else if (today == DayOfWeek.SATURDAY) {
				dayToAdd = 2;
			}
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		}
	}

	private static void useDateFormatter() {
		// 날짜와 시간 관련 포매팅과 파싱은 필수(DateTimeFormmatter 클래스는 BASIC_ISO_DATE와
		// ISO_LOCAL_DATE등의 상수를 미리 정의)
		// DateTimeFormmatter - 날짜나 시간을 특정 형식의 문자열로 제작 O
		LocalDate date = LocalDate.of(2014, 3, 18);
		String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
		String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18
		System.out.println(s1 + "\n" + s2);

		// 날짜나 시간을 표현하는 문자열을 파싱하여 날짜 객체를 다시 제작 O
		LocalDate parse = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
		LocalDate parse2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);
		System.out.println(parse + "\n" + parse2);

		// DateTimeFormatter - 특정 패턴으로 포매터를 만들 수 있는 정적 팩토리 메서드 제공
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.of(2014, 3, 18); // 2014-03-18
		String formattedDate = localDate.format(formatter); // 18/03/2014
		LocalDate parse1 = localDate.parse(formattedDate, formatter);
		System.out.println(formattedDate);

		// 지역화 된 DateTimeFormatter도 제작 O → 조금 더 복합적인 Formatter를 만들기 위해 Builder 사용
		DateTimeFormatter italianFormatter = new DateTimeFormatterBuilder().appendText(ChronoField.DAY_OF_MONTH)
				.appendLiteral(". ").appendText(ChronoField.MONTH_OF_YEAR).appendLiteral(" ")
				.appendText(ChronoField.YEAR).parseCaseInsensitive() // 정해진 형식과 정확하게 일치하지 않아도 해석가능
				.toFormatter(Locale.ITALIAN);

		System.out.println(date.format(italianFormatter));
	}

	private static void useZone() {
		// 시간대 사용하기 (ZoneRules 클래스 - 표준 시간이 같은 지역을 묶어 시간대 규칙 집합을 정의)
		ZoneId romeZone = ZoneId.of("Europe/Rome"); // 지역 Id = "지역/도시" 형식
		System.out.println(romeZone);

		// getDefault() - 기존의 TimeZone 객체를 ZoneId 객체로 변환 O
		ZoneId zoneId = TimeZone.getDefault().toZoneId();
		System.out.println(zoneId);

		// UTC/Greenwich 기준의 고정 오프셋 - ZoneOffset
//		ZoneOffset.of("-5:00"); // 현재 타임존 보다 5시간 느린 곳 정의(서머 타임을 정확히 처리 X → 권장 X)
	}
}
