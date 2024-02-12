package Chap05;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PuttingIntoPractice {
	public static void main(String[] args) {
		// 거래자 리스트
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");

		// 트랜잭션 리스트
		List<Transaction> transactions = Arrays.asList(new Transaction(brian, 2011, 300),
				new Transaction(raoul, 2012, 1000), new Transaction(raoul, 2011, 400),
				new Transaction(mario, 2012, 710), new Transaction(mario, 2012, 700), new Transaction(alan, 2012, 950));

		// 질의 1: 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
		List<Transaction> tr2011 = transactions.stream().filter(year -> year.getYear() == 2011)
				.sorted(comparing(Transaction::getValue)).collect(toList());

		// 질의2: 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
		List<String> city = transactions.stream().map(ct -> ct.getTrader().getCity()).distinct().collect(toList());

		// 질의3: 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.
		List<Trader> tradersInCambridge = transactions.stream().map(Transaction::getTrader) // 모든 거래자 추출
				.filter(td -> td.getCity().equals("Cambridge")).distinct().sorted(comparing(Trader::getName))
				.collect(toList());

		// 질의4: 모든 거래자의 이름을 알파벳순으로 정렬해서 하나의 문자열로 반환하시오.
		String traders = transactions.stream().map(td -> td.getTrader().getName()) // 모든 거래자명 추출
				.distinct().sorted().reduce("", (n1, n2) -> n1 + n2 + " ");
		System.out.println(traders);

		// 질의5: 밀라노에 거래자가 있는가?
		boolean hasMilano = transactions.stream().anyMatch(ct -> ct.getTrader().getCity().equals("Milan"));

		// 질의6: 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
		transactions.stream().filter(ct -> ct.getTrader().getCity().equals("Cambridge")).map(Transaction::getValue)
				.forEach(System.out::println);

		// 질의7: 전체 트랜잭션 중 최댓값은 얼마인가?
		Optional<Integer> tmax = transactions.stream().map(Transaction::getValue).reduce(Integer::max);

		// 질의8: 전체 트랜잭션 중 최솟값을 가지는 트랜잭션은 무엇인가?
		Optional<Transaction> tmin = transactions.stream().reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);
		System.out.println(tmin);
	}
}
