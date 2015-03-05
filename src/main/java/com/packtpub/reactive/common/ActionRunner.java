package com.packtpub.reactive.common;

import rx.functions.Action1;

public class ActionRunner {

	private final Action1<String[]> action;
	private final String[] arguments;

	public ActionRunner(Action1<String[]> action, String[] arguments) {
		this.action = action;
		this.arguments = arguments;
	}

	public void run() {
		this.action.call(this.arguments);
	}

}