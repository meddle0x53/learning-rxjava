package com.packtpub.reactive.one;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.packtpub.reactive.common.Program;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

public class IteratorExamples implements Program {

	public static void usingIteratorExample() {
		List<String> list = Arrays
				.asList("One", "Two", "Three", "Four", "Five"); // (1)

		Iterator<String> iterator = list.iterator(); // (2)

		try {
			while (iterator.hasNext()) { // 3
				System.out.println(iterator.next()); // Prints elements (4)
			}

			System.out.println("We've finnished!"); // (5)
			System.out.println(iterator.hasNext()); // Prints 'false' (5)
		} catch (NoSuchElementException e) {
			System.err.println(e); // (6)
		}

	}

	public static void usingObservableExample() {
		List<String> list = Arrays
				.asList("One", "Two", "Three", "Four", "Five");

		Observable<String> observable = Observable.from(list);
		observable.subscribe(new Action1<String>() {
			public void call(String element) {
				System.out.println(element);
			}
		}, new Action1<Throwable>() {
			public void call(Throwable t) {
				System.err.println(t); // (1)
			}
		}, new Action0() {
			public void call() {
				System.out.println("We've finnished!"); // (2)
			}
		});
	}

	public static void main(String[] args) {
	}

	@Override
	public String name() {
		return "Iterator vs Observable";
	}

	@Override
	public void run() {
		usingIteratorExample();
		usingObservableExample();
	}

	@Override
	public int chapter() {
		return 1;
	}
}
