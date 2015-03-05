package com.packtpub.reactive.three;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import com.packtpub.reactive.common.Program;

public class SubjectsExample implements Program {

	@Override
	public String name() {
		return "Subjects demonstration.";
	}

	@Override
	public int chapter() {
		return 3;
	}

	public static class ReactiveSum {
		private BehaviorSubject<Double> a = BehaviorSubject.create(0.0);
		private BehaviorSubject<Double> b = BehaviorSubject.create(0.0);
		private BehaviorSubject<Double> c = BehaviorSubject.create(0.0);

		public ReactiveSum() {
			Observable.combineLatest(a, b, (x, y) -> x + y).subscribe(c);
		}

		public double getA() {
			return a.getValue();
		}

		public void setA(double a) {
			this.a.onNext(a);
		}

		public Observable<Double> obsA() {
			return a.asObservable();
		}

		public double getB() {
			return b.getValue();
		}

		public void setB(double b) {
			this.b.onNext(b);
		}

		public Observable<Double> obsB() {
			return b.asObservable();
		}

		public double getC() {
			return c.getValue();
		}

		public Observable<Double> obsC() {
			return c.asObservable();
		}

	}

	@Override
	public void run() {

		Observable<Long> interval = Observable.interval(100L,
				TimeUnit.MILLISECONDS);

		Subject<Long, Long> publishSubject = PublishSubject.create();
		interval.subscribe(publishSubject);

		Subscription sub1 = subscribePrint(publishSubject, "First");
		Subscription sub2 = subscribePrint(publishSubject, "Second");

		Subscription sub3 = null;
		try {
			Thread.sleep(300L);

			publishSubject.onNext(555L);
			sub3 = subscribePrint(publishSubject, "Third");
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		sub1.unsubscribe();
		sub2.unsubscribe();
		sub3.unsubscribe();

		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		Subscription sub4 = subscribePrint(publishSubject, "Fourth");

		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		sub4.unsubscribe();

		System.out.println("-----------------------------");

		ReactiveSum sum = new ReactiveSum();

		subscribePrint(sum.obsC(), "Sum");
		sum.setA(5);
		sum.setB(4);
	}

}
