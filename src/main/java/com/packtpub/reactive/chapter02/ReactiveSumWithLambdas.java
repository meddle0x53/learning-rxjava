package com.packtpub.reactive.chapter02;

import java.util.regex.Pattern;

import rx.Observable;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;

public class ReactiveSumWithLambdas implements Program {

	public static Observable<Double> varStream(final String varName,
			Observable<String> input) {
		final Pattern pattern = Pattern.compile("\\s*" + varName
				+ "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");

		return input
				.map(str -> pattern.matcher(str))
				.filter(matcher -> matcher.matches()
						&& matcher.group(1) != null)
				.map(matcher -> matcher.group(1))
				.map(str -> Double.parseDouble(str));
	}

	public static void reactiveSum(Observable<Double> a, Observable<Double> b) {
		Observable.combineLatest(a, b, (x, y) -> x + y).subscribe(
				sum -> System.out.println("update : a + b = " + sum),
				error -> {
					System.out.println("Got an error!");
					error.printStackTrace();
				}, () -> System.out.println("Exiting..."));
	}

	public String name() {
		return "Reactive Sum : Lambdas";
	}

	public void run() {
		Observable<String> input = CreateObservable.input();

		Observable<Double> a = varStream("a", input);
		Observable<Double> b = varStream("b", input);

		reactiveSum(a, b);

	}

	public static void main(String[] args) {
		new ReactiveSumWithLambdas().run();
	}

	@Override
	public int chapter() {
		return 2;
	}
}
