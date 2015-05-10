package com.packtpub.reactive.chapter02;

import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;

/**
 * Version 2 of the Reactive Sum, implemented using Java 8 lambdas.
 * 
 * @author meddle
 */
public class ReactiveSumV2 implements Program {
	
	private CountDownLatch latch = new CountDownLatch(1);

	public static Observable<Double> varStream(final String varName,
			Observable<String> input) {
		final Pattern pattern = Pattern.compile("\\s*" + varName
				+ "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)");

		return input
				.map(pattern::matcher)
				.filter(matcher -> matcher.matches()
						&& matcher.group(1) != null)
				.map(matcher -> matcher.group(1))
				.map(Double::parseDouble);
	}

	public void reactiveSum(Observable<Double> a, Observable<Double> b) {

		Observable
		.combineLatest(a.startWith(0.0), b.startWith(0.0), (x, y) -> x + y)
		.subscribeOn(Schedulers.io())
		.subscribe(
				sum -> System.out.println("update : a + b = " + sum),
				error -> {
					System.out.println("Got an error!");
					error.printStackTrace();
				}, () -> {
					System.out.println("Exiting...");
					latch.countDown();
				});
		
	}

	public String name() {
		return "Reactive Sum, version 2 (with lambdas)";
	}

	public void run() {
		ConnectableObservable<String> input = CreateObservable.from(System.in);

		Observable<Double> a = varStream("a", input);
		Observable<Double> b = varStream("b", input);

		reactiveSum(a, b);
		
		input.connect();

		try {
			latch.await();
		} catch (InterruptedException e) {}
	}

	@Override
	public int chapter() {
		return 2;
	}

	public static void main(String[] args) {
		new ReactiveSumV2().run();
	}
}
