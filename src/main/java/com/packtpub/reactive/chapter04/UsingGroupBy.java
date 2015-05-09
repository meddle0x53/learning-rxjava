package com.packtpub.reactive.chapter04;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

import com.packtpub.reactive.common.Program;

/**
 * Demonstrates how the groupBy operator can be used.
 * 
 * @author meddle
 */
public class UsingGroupBy implements Program {


	@Override
	public String name() {
		return "Demonstration of using the Observable#groupBy operator";
	}

	@Override
	public int chapter() {
		return 4;
	}

	@Override
	public void run() {
		List<String> albums = Arrays.asList(
				"The Piper at the Gates of Dawn", "A Saucerful of Secrets", "More",
				"Ummagumma", "Atom Heart Mother", "Meddle", "Obscured by Clouds",
				"The Dark Side of the Moon", "Wish You Were Here", "Animals",
				"The Wall");

		Observable.from(albums).groupBy(album -> album.split(" ").length)
				.subscribe(obs -> {
					subscribePrint(obs, obs.getKey() + " word(s)");
				});

		Observable
				.from(albums)
				.groupBy(album -> album.replaceAll("[^mM]", "").length(),
						album -> album.replaceAll("[mM]", "*"))
				.subscribe(
						obs -> subscribePrint(obs, obs.getKey()
								+ " occurences of 'm'"));

	}
	
	public static void main(String[] args) {
		new UsingGroupBy().run();
	}

}
