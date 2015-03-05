package com.packtpub.reactive.one;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.functions.Func2;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;

public class ReactiveSumExample implements Program {

	public static final class ReactiveSum implements Observer<Double> {

		private double sum;

		public ReactiveSum(Observable<Double> a, Observable<Double> b) {
			this.sum = 0;

			Observable.combineLatest(a, b, new Func2<Double, Double, Double>() {
				public Double call(Double a, Double b) {
					return a + b;
				}
			}).subscribe(this);
		}

		public void onCompleted() {
			System.out.println("Exiting last sum was : " + this.get());
		}

		public void onError(Throwable e) {
			System.err.println("Got an error!");
			e.printStackTrace();
		}

		public void onNext(Double sum) {
			System.out.println("update : a + b = " + sum);
			this.sum = sum;
		}

		public double get() {
			return this.sum;
		}
	}

	public static Observable<Double> varStream(final String varName,
			Observable<String> input) {
		final Pattern pattern = Pattern.compile("\\s*" + varName
				+ "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");

		return input.map(new Func1<String, Matcher>() {
			public Matcher call(String str) {
				return pattern.matcher(str);
			}
		}).filter(new Func1<Matcher, Boolean>() {
			public Boolean call(Matcher matcher) {
				return matcher.matches() && matcher.group(1) != null;
			}
		}).map(new Func1<Matcher, String>() {
			public String call(Matcher matcher) {
				return matcher.group(1);
			}
		}).filter(new Func1<String, Boolean>() {
			public Boolean call(String str) {
				return str != null;
			}
		}).map(new Func1<String, Double>() {
			public Double call(String str) {
				return Double.parseDouble(str);
			}
		});
	}

	public String name() {
		return "Reactive Sum : Intro";
	}

	public void run() {
		Observable<String> input = CreateObservable.input();

		Observable<Double> a = varStream("a", input);
		Observable<Double> b = varStream("b", input);

		new ReactiveSum(a, b);
	}

	@Override
	public int chapter() {
		return 1;
	}
}
