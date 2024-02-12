package Chap09;

abstract class OnlineBanking {

	// 9.2.2
	// processCustomer가 알고리즘 개요를 제시
	// OnlineBanking을 상속받는 구현체가 makeCustomerHappy 메서드를 구현하여 알고리즘을 구현
	public void processCustomer(int id) {
		Customer c = Database.getCustomerWithId(id);
		makeCustomerHappy(c);
	}

	abstract void makeCustomerHappy(Customer c);

	// 더미 Customer 클래스
	static private class Customer {
	}

	// 더미 Database 클래스
	static private class Database {
		static Customer getCustomerWithId(int id) {
			return new Customer();
		}
	}
}
