package com.packtpub.reactive.two;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.packtpub.reactive.common.Program;

public class IntroToLambdasExample implements Program {

	interface Mapper<V, M> {
		M map(V value);
	}

	interface Action<V> {
		void act(V value);
	}

	public static <V, M> List<M> map(List<V> list, Mapper<V, M> mapper) {
		List<M> mapped = new ArrayList<M>(list.size());
		for (V v : list) {
			mapped.add(mapper.map(v));
		}

		return mapped;
	}

	public static <V> void act(List<V> list, Action<V> action) {
		for (V v : list) {
			action.act(v);
		}
	}

	@Override
	public String name() {
		return "Intro to lambdas.";
	}

	@Override
	public int chapter() {
		return 2;
	}

	@Override
	public void run() {
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		List<Integer> mapped = map(numbers, new Mapper<Integer, Integer>() {
			@Override
			public Integer map(Integer value) {
				return value * value;
			}
		});

		Consumer<String> print = System.out::println;
		print.andThen(print).accept("Here we go!");

		Function<Integer, String> map = (value) -> (value + "!");
		print.accept(map.apply(5));

		Mapper<Integer, String> toMessage = (value) -> "We are happy to present to you : "
				+ (value) + "!";
		List<String> messages = map(mapped, toMessage);

		Predicate<Integer> odd = (value) -> value % 2 != 0;
		print.accept(odd.test(5) + "");

		act(messages, System.out::println);

		List<String> lambdaMapped = map(numbers, v -> {
			return "The square of " + v + " is " + (v * v) + "!";
		});
		act(lambdaMapped, v -> System.out.println(v));

	}
}
