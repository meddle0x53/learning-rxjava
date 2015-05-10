package com.packtpub.reactive.chapter05;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

/**
 * Demonstrates working with {@link Observable#onErrorReturn}, {@link Observable#onErrorResumeNext} and {@link Observable#onExceptionResumeNext}
 * as well as retrying with {@link Observable#retry} and {@link Observable#retryWhen}.
 * 
 * @author meddle
 */
public class HandlingErrors implements Program {
	
	class FooException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public FooException() {
			super("Foo!");
		}
	}

	class BooException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public BooException() {
			super("Boo!");
		}
	}
	
	class ErrorEmitter implements OnSubscribe<Integer> {

		private int throwAnErrorCounter = 5;
		
		@Override
		public void call(Subscriber<? super Integer> subscriber) {
			subscriber.onNext(1);
			subscriber.onNext(2);

			if (throwAnErrorCounter > 4) {
				throwAnErrorCounter--;
				subscriber.onError(new FooException());
				return;
			}
			if (throwAnErrorCounter > 0) {
				throwAnErrorCounter--;
				subscriber.onError(new BooException());
				return;
			}

			subscriber.onNext(3);
			subscriber.onNext(4);
			subscriber.onCompleted();

		}
	}
	
	@Override
	public String name() {
		return "Examples of handling errors";
	}

	@Override
	public int chapter() {
		return 5;
	}

	@Override
	public void run() {
		
		Observable<String> numbers = Observable.just("1", "2", "three", "4", "5");
		
		Observable<Integer> n = numbers.map(Integer::parseInt).onErrorReturn(e -> -1);

		subscribePrint(n, "error returned");
		
		Observable<Integer> defaultOnError = Observable.just(5, 4, 3, 2, 1);

		n = numbers.map(Integer::parseInt).onErrorResumeNext(defaultOnError);

		subscribePrint(n, "on error resume next");
		
		n = numbers.map(Integer::parseInt).onExceptionResumeNext(defaultOnError);

		subscribePrint(n, "on exception resume next");
		
		n = numbers.doOnNext(number -> {
			assert !number.equals("three");
			}).map(Integer::parseInt).onExceptionResumeNext(defaultOnError);

		
		subscribePrint(n, "on exception resume next 2");

		n = numbers.doOnNext(number -> {
			assert !number.equals("three");
			}).map(Integer::parseInt).onErrorResumeNext(defaultOnError);

		subscribePrint(n, "on error resume next 2");
		
		subscribePrint(Observable.create(new ErrorEmitter()).retry(), "Retry");
		
		Observable<Integer> when = Observable.create(new ErrorEmitter()).retryWhen(attempts -> {
			return attempts.flatMap(error -> {
				if (error instanceof FooException) {
					System.err.println("Delaying...");
					return Observable.timer(1L, TimeUnit.SECONDS, Schedulers.immediate());
				}
				
				return Observable.error(error);
			});
		}).retry((attempts, error) -> {
			return (error instanceof BooException) && attempts < 3;
		});
		
		subscribePrint(when, "retryWhen");
		
	}
	
	public static void main(String[] args) {
		new HandlingErrors().run();
	}

}
