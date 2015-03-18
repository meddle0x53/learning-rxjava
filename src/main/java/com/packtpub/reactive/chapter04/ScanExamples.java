package com.packtpub.reactive.chapter04;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.beans.Introspector;
import java.nio.file.Paths;

import rx.Observable;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;

public class ScanExamples implements Program {

	@Override
	public String name() {
		return "Examples of using scan.";
	}

	@Override
	public int chapter() {
		return 4;
	}

	@Override
	public void run() {
		Observable<Integer> scan = Observable.range(1, 10)
				.scan((p, v) -> p + v);

		subscribePrint(scan, "Sum");

		subscribePrint(scan.last(), "Final sum");

		Observable<String> file = CreateObservable.from(Paths.get("src",
				"main", "resources", "letters.txt"));

		scan = file.scan(0, (p, v) -> p + 1);

		subscribePrint(scan.last(), "wc -l");

		file = CreateObservable.from(Paths.get("src", "main", "resources",
				"operators.txt"));

		Observable<String> multy = file
				.flatMap(line -> Observable.from(line.split("\\.")))
				.map(String::trim)
				.map(sentence -> sentence.split(" "))
				.filter(array -> array.length > 0)
				.map(array -> array[0])
				.distinct()
				.groupBy(word -> word.contains("'") || word.equals("God"))
				.flatMap(
						observable -> observable.getKey() ? observable
								: observable.map(Introspector::decapitalize))
				.map(String::trim).filter(word -> !word.isEmpty())
				.scan((current, word) -> current + " " + word).last()
				.map(sentence -> sentence + ".");
		subscribePrint(multy, "Multiple operators");
	}

}
