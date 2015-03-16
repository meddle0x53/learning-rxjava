package com.packtpub.reactive.one;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.packtpub.reactive.common.Program;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Demonstrate the differences between Iterators and Observables.
 * 
 * @author meddle
 */
public class ObservableVSIterator implements Program {

	private static void usingIteratorExample() {
		List<String> list = Arrays
				.asList("One", "Two", "Three", "Four", "Five");

		Iterator<String> iterator = list.iterator();

		try {
			// While there is a next element, PULL it from the source and print it.
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}

			System.out.println("We've finnished!");
			System.out.println(iterator.hasNext());
		} catch (NoSuchElementException e) {
			System.err.println(e);
		}

	}

	private static void usingObservableExample() {
		List<String> list = Arrays
				.asList("One", "Two", "Three", "Four", "Five");

		Observable<String> observable = Observable.from(list);
		
		// Subscribe to the Observable. It will PUSH it's values to the Subscriber, and it will be printed.
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
