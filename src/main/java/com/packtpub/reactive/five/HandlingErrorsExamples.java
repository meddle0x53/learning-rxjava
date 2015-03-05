package com.packtpub.reactive.five;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

public class HandlingErrorsExamples implements Program {
	
	private int throwAnError = 3;

	public int getThrowAnError() {
		return throwAnError;
	}
	
	public void setThrowAnError(int throwAnError) {
		this.throwAnError = throwAnError;
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
		
		Observable<Integer> n = numbers.map(Integer::parseInt).onErrorReturn(e -> 3);

		subscribePrint(n, "error returned");

		n = numbers.map(Integer::parseInt).onErrorResumeNext(e -> Observable.just(3, 4, 5));

		subscribePrint(n, "on error resume next");
		
		n = numbers.map(Integer::parseInt).onExceptionResumeNext(Observable.just(4, 5, 6));

		subscribePrint(n, "on exception resume next");
		
		n = numbers.doOnNext(number -> {
			assert !number.equals("three");
			}).map(Integer::parseInt).onExceptionResumeNext(Observable.just(4, 5, 6));

		
		subscribePrint(n, "on exception resume next 2");

		n = numbers.doOnNext(number -> {
			assert !number.equals("three");
			}).map(Integer::parseInt).onErrorResumeNext(Observable.just(5, 6, 7));

		
		subscribePrint(n, "on error resume next 2");
		
		
		Observable<Integer> errors = Observable.<Integer>create(subscriber -> {
			subscriber.onNext(1);
			subscriber.onNext(2);
			
			if (getThrowAnError() > 4) {
				setThrowAnError(getThrowAnError() - 1);
				subscriber.onError(new RuntimeException("Foo!"));
				return;
			}
			if (getThrowAnError() > 0) {
				setThrowAnError(getThrowAnError() - 1);
				subscriber.onError(new RuntimeException("Boo!"));
				return;
			}
			
			subscriber.onNext(3);
			subscriber.onNext(4);
			subscriber.onCompleted();
		});
		
		subscribePrint(errors.retry(), "Retry");
		
		setThrowAnError(5);
		
		Observable<Integer> when = errors.retryWhen(attempts -> {
			return attempts.flatMap(error -> {
				if (error.getMessage().equals("Foo!")) {
					System.err.println("Delaying...");
					return Observable.timer(1L, TimeUnit.SECONDS, Schedulers.immediate());
				}
				
				return Observable.error(error);
			});
		}).retry((attempts, error) -> {
			return error.getMessage().equals("Boo!") && attempts < 3;
		});
		
		subscribePrint(when, "retryWhen");
		
	}

}
