package com.packtpub.reactive.chapter08;

import java.nio.file.Path;
import java.nio.file.Paths;

import rx.Observable;
import rx.Observable.Transformer;

import com.packtpub.reactive.chapter08.LiftExample.Indexed;
import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Helpers;
import com.packtpub.reactive.common.Program;

public class ComposeExample implements Program {
	
	public static class OddFilter<T> implements Transformer<T, T> {

		@Override
		public Observable<T> call(Observable<T> observable) {
			return observable
					.lift(new Indexed<T>(1L))
					.filter(pair -> pair.getLeft() % 2 == 1)
					.map(pair -> pair.getRight());
		}
		
	}

	@Override
	public String name() {
		return "Demonstrates using the Observable.compose operator.";
	}

	@Override
	public int chapter() {
		return 8;
	}

	@Override
	public void run() {
		Path path = Paths.get("src", "main", "resources", "letters.txt");
		Observable<?> indexedStrings = CreateObservable
				.from(path).compose(new OddFilter<String>());
		
		Helpers.subscribePrint(indexedStrings, "Indexed strings");
	}
	
	public static void main(String[] args) {
		new ComposeExample().run();
	}

}
