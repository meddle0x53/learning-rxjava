package com.packtpub.reactive.two;

import java.util.function.Function;

import com.packtpub.reactive.common.Program;

public class MonadsExample implements Program {

	@Override
	public String name() {
		return "Monads";
	}

	@Override
	public int chapter() {
		return 2;
	}

	public static class Opt<T> {

		public static Opt<?> NULL = new Opt<>();

		private final T data;

		private Opt(T data) {
			this.data = data;
		}

		private Opt() {
			this.data = null;
		}

		public T get() {
			return this.data;
		}

		public static <T> Opt<T> unit(T data) {
			return data == null ? nil() : new Opt<>(data);
		}

		@SuppressWarnings("unchecked")
		public static <T> Opt<T> nil() {
			return (Opt<T>) NULL;
		}

		public <R> Opt<R> bind(Function<T, Opt<R>> f) {
			if (this.data == null) {
				return nil();
			}

			return f.apply(get());
		}
	}

	public static class Address {
		private final String street;

		public Address(String street) {
			this.street = street;
		}

		public String getStreet() {
			return this.street;
		}

		public Opt<String> getOptStreet() {
			return Opt.<String> unit(this.street);
		}
	}

	public static class User {
		private final Address address;

		public User() {
			this.address = null;
		}

		public User(Address address) {
			this.address = address;
		}

		public Address getAddress() {
			return this.address;
		}

		public Opt<Address> getOptAddress() {
			return Opt.<Address> unit(this.address);
		}
	}

	@Override
	public void run() {
		User user = new User(new Address("14 Penny Street"));
		System.out.println(user.getAddress().getStreet());

		user = new User();

		String street = user.getOptAddress().bind(a -> a.getOptStreet()).get();
		System.out.println(street);

		// Associativity
		Function<String, Opt<Integer>> strToInt = s -> Opt.unit(Integer
				.parseInt(s));
		Function<Integer, Opt<Integer>> pow = v -> Opt.unit(v * v);

		Opt<String> str = Opt.<String> unit("4");
		Opt<Integer> left = (str.bind(strToInt)).bind(pow);
		Opt<Integer> right = str.bind(s -> strToInt.apply(s).bind(pow));
		System.out.println(left.get() == right.get());

		// Left identity
		left = Opt.unit("5").bind(strToInt);
		right = strToInt.apply("5");
		System.out.println(left.get() == right.get());

		// Right identity
		Opt<Integer> monad1 = Opt.unit(9);
		Opt<Integer> monad2 = monad1.bind(Opt::unit);
		System.out.println(monad1.get() == monad2.get());

	}

}
