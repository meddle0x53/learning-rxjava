package com.packtpub.reactive.chapter02;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.functions.Func1;

import com.packtpub.reactive.common.Program;

public class ObservablesAndMonads implements Program {

	@Override
	public String name() {
		return "Observables and monads";
	}

	@Override
	public int chapter() {
		return 2;
	}

	@Override
	public void run() {
		Func1<Integer, Observable<Integer>> twoDevidedByN = number -> {
			return Observable.just(2 / number);
		};

		AtomicInteger left = new AtomicInteger();
		AtomicInteger right = new AtomicInteger();

		try {
			Observable
					.just(0)
					.flatMap(twoDevidedByN)
					.subscribe(n -> left.set(n),
							t -> left.set(Integer.MAX_VALUE));

			twoDevidedByN.call(0).subscribe(n -> right.set(n),
					t -> right.set(Integer.MAX_VALUE));
		} catch (Exception e) {
			// pass
		}

		System.out.println(left.equals(right));
	}

}
