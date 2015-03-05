package com.packtpub.reactive.common;

import rx.Subscriber;
import rx.Observable.Operator;

public class WithIndex<T> implements Operator<IndexedValue<T>, T> {
	int counter = 0;

	@Override
	public Subscriber<? super T> call(Subscriber<? super IndexedValue<T>> t1) {
		return new Subscriber<T>() {
			@Override
			public void onCompleted() {
				t1.onCompleted();
			}

			@Override
			public void onError(Throwable e) {
				t1.onError(e);
			}

			@Override
			public void onNext(T t) {
				t1.onNext(new IndexedValue<T>(++counter, t));
			}
		};
	}
}