package com.packtpub.reactive.common;

import rx.Observable;
import rx.Observable.Transformer;
import rx.observables.GroupedObservable;

public class IndexAndGroupChapterExamples implements Transformer<Program, GroupedObservable<Integer, String>> {

	@Override
	public Observable<GroupedObservable<Integer, String>> call(
			Observable<Program> source) {
		Observable<GroupedObservable<Integer, String>> result = source
				.lift(new WithIndex<>())
				.groupBy(
						indexed -> indexed.getValue().chapter(),
						indexed -> "  " + indexed.getIndex() + ". " + indexed.getValue().name()
						);
		return result;
	}

}
