package com.packtpub.reactive.common;

public class IndexedValue<T> {

	private final int index;
	private final T value;

	public IndexedValue(int index, T value) {
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public T getValue() {
		return value;
	}
}