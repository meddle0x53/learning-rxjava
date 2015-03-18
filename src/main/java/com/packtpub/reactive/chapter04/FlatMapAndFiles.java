package com.packtpub.reactive.chapter04;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.nio.file.Paths;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;

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
				"{lorem.txt,letters.txt}").flatMap(
				path -> CreateObservable.from(path, Schedulers.io())
						.onBackpressureBuffer());

		subscribePrint(fsObs, "FS");

		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
		}
	}

}
