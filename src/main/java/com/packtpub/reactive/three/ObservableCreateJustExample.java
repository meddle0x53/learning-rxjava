package com.packtpub.reactive.three;

import rx.Observable;

import com.packtpub.reactive.common.Program;

public class ObservableCreateJustExample implements Program {

	@Override
	public String name() {
		return "Using the Observable.just method to create Observables";
	}

	@Override
	public int chapter() {
		return 3;
	}

	public static class User {
		private final String forename;
		private final String lastname;

		public User(String forename, String lastname) {
			this.forename = forename;
			this.lastname = lastname;
		}

		public String getForename() {
			return forename;
		}

		public String getLastname() {
			return lastname;
		}
	}

	@Override
	public void run() {
		Observable.just('r', 'x', 'J', 'a', 'v', 'a').subscribe(
				System.out::print, System.err::println, System.out::println);

		Observable.just(new User("Dali", "Bali"))
				.map(u -> u.getForename() + " " + u.getLastname())
				.subscribe(System.out::println);
	}

}
