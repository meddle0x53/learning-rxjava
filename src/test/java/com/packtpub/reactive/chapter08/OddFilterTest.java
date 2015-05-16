package com.packtpub.reactive.chapter08;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import rx.Observable;
import rx.Observable.Transformer;

import com.packtpub.reactive.chapter08.Compose.OddFilter;

/**
 * Test for the {@link OddFilter} {@link Transformer}.
 * 
 * @author meddle
 */
public class OddFilterTest {

	@Test
	public void testFiltersOddOfTheSequence() {
		
		Observable<String> tested = Observable
				.just("One", "Two", "Three", "Four", "Five", "June", "July")
				.compose(new OddFilter<String>());
		
		List<String> expected =
				Arrays.asList("One", "Three", "Five", "July");
		
		List<String> actual = tested
				.toList()
				.toBlocking()
				.single();
		
		assertEquals(expected, actual);
	}

}
