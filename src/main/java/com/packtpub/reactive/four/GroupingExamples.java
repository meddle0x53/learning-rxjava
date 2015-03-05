package com.packtpub.reactive.four;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

import com.packtpub.reactive.common.Program;
import static com.packtpub.reactive.common.Helpers.subscribePrint;

public class GroupingExamples implements Program {

	private final List<String> albums = Arrays.asList(
			"The Piper at the Gates of Dawn", "A Saucerful of Secrets", "More",
			"Ummagumma", "Atom Heart Mother", "Meddle", "Obscured by Clouds",
			"The Dark Side of the Moon", "Wish You Were Here", "Animals",
			"The Wall");

	@Override
	public String name() {
		return "Example of grouping";
	}

	@Override
	public int chapter() {
		return 4;
	}

	static class Tester<K> {

		private final K attr;

		public Tester(K attr) {
			this.attr = attr;
		}

		public K getAttr() {
			return attr;
		}

		public static <L> Tester<L> make(L attr) {
			return new Tester<L>(attr);
		}
	}

	@Override
	public void run() {
		Observable.from(albums).groupBy(album -> album.split(" ").length)
				.subscribe(obs -> {
					subscribePrint(obs, obs.getKey() + " word(s)");
				});

		Observable
				.from(albums)
				.groupBy(album -> album.replaceAll("[^m|^M]", "").length(),
						album -> album.replaceAll("[m|M]", "*"))
				.subscribe(
						obs -> subscribePrint(obs, obs.getKey()
								+ " occurences of 'm'"));

	}

}
