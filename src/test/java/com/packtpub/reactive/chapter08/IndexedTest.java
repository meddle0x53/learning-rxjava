package com.packtpub.reactive.chapter08;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;

import com.packtpub.reactive.chapter08.Lift.Indexed;
import com.packtpub.reactive.chapter08.Lift.Pair;

/**
 * Tests the {@link Indexed} operator.
 * 
 * @author meddle
 */
public class IndexedTest {

	@Test
	public void testGeneratesSequentialIndexes() {
		Observable<Pair<Long, String>> observable = Observable
				.just("a", "b", "c", "d", "e")
				.lift(new Indexed<String>());
		
		List<Pair<Long, String>> expected = Arrays.asList(
				new Pair<Long, String>(0L, "a"),
				new Pair<Long, String>(1L, "b"),
				new Pair<Long, String>(2L, "c"),
				new Pair<Long, String>(3L, "d"),
				new Pair<Long, String>(4L, "e")
				);
		
		List<Pair<Long, String>> actual = observable
				.toList()
				.toBlocking().
				single();
		
		assertEquals(expected, actual);
		
		// The same result the second time around
		TestSubscriber<Pair<Long, String>> testSubscriber = new TestSubscriber<Pair<Long, String>>();
		observable.subscribe(testSubscriber);
		
		testSubscriber.assertReceivedOnNext(expected);
		
	}

}
