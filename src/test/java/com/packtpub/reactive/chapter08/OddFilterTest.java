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
		
		Observable<Integer> tested = Observable
				.range(2, 10)
				.compose(new OddFilter<Integer>());
		
		List<Integer> expected =
				Arrays.asList(2, 4, 6, 8, 10);
		
		List<Integer> actual = tested
				.toList()
				.toBlocking()
				.single();
		
		assertEquals(expected, actual);
	}

}
