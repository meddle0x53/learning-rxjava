package com.packtpub.reactive.chapter02;

import java.util.function.Function;
import java.util.function.Predicate;

import com.packtpub.reactive.common.Program;

/**
 * Demonstrates pure and higher order functions.
 * 
 * @author meddle
 */
public class PureAndHigherOrderFunctions implements Program {

	@Override
	public String name() {
		return "Pure and higher functions";
	}

	@Override
	public int chapter() {
		return 2;
	}

	@Override
	public void run() {
		// A pure function - no side effects
		Predicate<Integer> even = (number) -> number % 2 == 0;

		// Not a pure function - with a side effect - prints something and then returns.
		Predicate<Integer> impureEven = (number) -> {
			System.out.println("Printing here is side effect!");
			return number % 2 == 0;
		};

		int i = 5;
		while ((i--) > 0) {
			System.out.println("Is five even? - " + even.test(5));
			System.out.println("Is five even? - " + impureEven.test(5));
		}

		// A pure function - converts string to integer
		Function<String, Integer> strToInt = s -> Integer.parseInt(s);

		// Some examples of applying higher order functions.
		System.out.println(highSum(v -> v * v, v -> v * v * v, 3, 2));
		System.out.println(highSum(strToInt, strToInt, "4", "5"));
		System.out.println(highSum(strToInt, "4", "5"));

		System.out.println(greet("Hello").apply("world"));
		System.out.println(greet("Goodbye").apply("cruel world"));

		Function<String, String> howdy = greet("Howdy");
		System.out.println(howdy.apply("Tanya"));
		System.out.println(howdy.apply("Dali"));
	}

	/**
	 * A higher order function - sums the results of two other functions, passed to it as parameters.
	 */
	public static <T, R> int highSum(Function<T, Integer> f1,
			Function<R, Integer> f2, T data1, R data2) {
		return f1.apply(data1) + f2.apply(data2);
	}

	public static <T> int highSum(Function<T, Integer> f, T data1, T data2) {
		return highSum(f, f, data1, data2);
	}

	/**
	 * A higher order function - returns a function.
	 */
	public static Function<String, String> greet(String greeting) {
		return (String name) -> greeting + " " + name + "!";
	}
	
	public static void main(String[] args) {
		new PureAndHigherOrderFunctions().run();
	}

}
