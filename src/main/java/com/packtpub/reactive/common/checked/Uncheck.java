package com.packtpub.reactive.common.checked;

import rx.functions.Action1;
import rx.functions.Func0;

public class Uncheck {
	
	public static <R> Func0<R> uncheckedFunc0(CheckedFunc0<R> f) {
		return () -> {
			try {
				return f.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
	
	public static <T> Action1<? super T> uncheckedAction1(CheckedAction1<? super T> a) {
		return arg -> {
			try {
				a.call(arg);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

}
