package com.packtpub.reactive.chapter07;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import com.packtpub.reactive.common.CreateObservable;

/**
 * A unit test testing the custom {@link CreateObservable#interval} method.
 * 
 * @author meddle
 */
public class CreateObservableIntervalTest {

	@Test(expected = IllegalArgumentException.class) 
	public void testExceptionWithNoGaps() {
		CreateObservable.interval();
	}
	
	@Test
	public void testBehavesAsNormalIntervalWithOneGap() throws Exception {
		TestScheduler testScheduler = Schedulers.test();
		Observable<Long> interval = CreateObservable.interval(
				Arrays.asList(100L), TimeUnit.MILLISECONDS, testScheduler
				);
		
		TestSubscriber<Long> subscriber = new TestSubscriber<Long>();
		interval.subscribe(subscriber);
		
		assertTrue(subscriber.getOnNextEvents().isEmpty());
		
		testScheduler.advanceTimeBy(101L, TimeUnit.MILLISECONDS);
		assertEquals(Arrays.asList(0L), subscriber.getOnNextEvents());
		
		testScheduler.advanceTimeBy(101L, TimeUnit.MILLISECONDS);
		assertEquals(Arrays.asList(0L, 1L), subscriber.getOnNextEvents());
		
		testScheduler.advanceTimeTo(1L, TimeUnit.SECONDS);
		assertEquals(Arrays.asList(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L), subscriber.getOnNextEvents());
	}

	@Test
	public void testWithMabyGaps() throws Exception {
		TestScheduler testScheduler = Schedulers.test();
		Observable<Long> interval = CreateObservable.interval(
				Arrays.asList(100L, 200L, 300L), TimeUnit.MILLISECONDS, testScheduler
				);
		
		TestSubscriber<Long> subscriber = new TestSubscriber<Long>();
		interval.subscribe(subscriber);
		
		assertTrue(subscriber.getOnNextEvents().isEmpty());
		
		testScheduler.advanceTimeBy(100L, TimeUnit.MILLISECONDS);
		assertEquals(Arrays.asList(0L), subscriber.getOnNextEvents());
		
		testScheduler.advanceTimeBy(100L, TimeUnit.MILLISECONDS);
		assertEquals(Arrays.asList(0L), subscriber.getOnNextEvents());
		
		testScheduler.advanceTimeTo(1L, TimeUnit.SECONDS);
		assertEquals(Arrays.asList(0L, 1L, 2L, 3L, 4L), subscriber.getOnNextEvents());
	}

}
