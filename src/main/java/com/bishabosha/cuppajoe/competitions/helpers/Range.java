/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.competitions.helpers;

public abstract class Range<T> {
	protected T max;
	protected T min;
	
	public Range(T min, T max) {
		this.max = max;
		this.min = min;
	}
	
	public T getMax() {
		return this.max;
	}
	
	public T getMin() {
		return this.min;
	}
	
	public abstract boolean inRange(T value);
	
	public abstract boolean hitLimitMax(T value);
	
	public abstract boolean hitLimitMin(T value);
}