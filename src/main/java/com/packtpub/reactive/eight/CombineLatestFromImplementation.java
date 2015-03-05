package com.packtpub.reactive.eight;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.concurrent.atomic.AtomicReference;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

public class CombineLatestFromImplementation implements Program {

	@Override
	public String name() {
		return "A simple implementation of RX's combineLatestFrom in Java.";
	}

	@Override
	public int chapter() {
		return 8;
	}

	@SuppressWarnings("unchecked")
	public <T, U, V> Observable<T> combineLatestFrom(Observable<U> o1,
			Observable<V> o2, Func2<U, V, T> f) {
		final Object nothing = new Object();
		return Observable.create(s -> {

			AtomicReference<V> val2 = new AtomicReference<V>((V) nothing);

			o1.subscribe(v -> {
				val2.getAndUpdate(current -> {
					if (current != nothing) {
						s.onNext(f.call(v, current));
					}
					return current;
				});

			}, s::onError, s::onCompleted);

			o2.subscribe(val2::set, s::onError);

		});
	}

	@Override
	public void run() {
		Observable<Integer> numbers = Observable.<Integer> create(s -> {
			s.onNext(1);

			try {
				Thread.sleep(1000L);
				s.onNext(2);

				Thread.sleep(4000L);
				s.onNext(3);

				Thread.sleep(500L);
				s.onNext(4);

				Thread.sleep(1000L);
				s.onNext(5);
			} catch (Exception e) {
				s.onError(e);
			}

			s.onCompleted();
		}).subscribeOn(Schedulers.newThread());

		Observable<String> letters = Observable.<String> create(s -> {

			try {
				Thread.sleep(500L);
				s.onNext("A");

				Thread.sleep(700L);
				s.onNext("B");

				Thread.sleep(2500L);
				s.onNext("C");

				Thread.sleep(200L);
				s.onNext("D");
			} catch (Exception e) {
				s.onError(e);
			}

			s.onCompleted();
		}).subscribeOn(Schedulers.newThread());

		subscribePrint(combineLatestFrom(numbers, letters, (n, l) -> n + l),
				"T");

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
		}
	}

}
