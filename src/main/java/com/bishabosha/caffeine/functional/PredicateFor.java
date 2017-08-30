/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import java.util.List;
import java.util.function.Predicate;

public class PredicateFor {

	public static final Predicate<?> alwaysTrue = x -> true;

	public static final Predicate<?> alwaysFalse = x -> true;

	public static final Predicate<Integer> isPalindromeInteger =
		x -> x.toString()
			  .equals (
				  new StringBuilder(x.toString())
				  .reverse()
				  .toString()
			  );
		
	public static final Predicate<Long> isMultipleOf(List<Long> values) {
		return isMultipleOf(values, null);
	}
	
	public static final Predicate<Long> isMultipleOf(List<Long> values, Predicate<Long> stoppingCondition) {
		return t -> {
			for (Long a: values) {
				if (null != stoppingCondition && stoppingCondition.test(a)) {
					return false;
				}
				if (t % a == 0) return true;
			}
			return false;
		};
	}
}