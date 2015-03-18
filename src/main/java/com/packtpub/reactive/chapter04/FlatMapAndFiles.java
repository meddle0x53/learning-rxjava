package com.packtpub.reactive.chapter04;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.nio.file.Paths;

import rx.Observable;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;

/**
 * Demonstration of using flatMap with an Observable created by directory stream,
 * reading all the files from it, using Observables.
 * 
 * @author meddle
 */
public class FlatMapAndFiles implements Program {

	@Override
	public String name() {
		return "Working with files using flatMap";
	}

	@Override
	public int chapter() {
		return 4;
	}

	@Override
	public void run() {
		Observable<String> fsObs = CreateObservable.listFolder(
				Paths.get("src", "main", "resources"),
				"{lorem.txt,letters.txt}")
				.flatMap(path -> CreateObservable.from(path));

		subscribePrint(fsObs, "FS");
	}
	
	public static void main(String[] args) {
		new FlatMapAndFiles().run();
	}

}
