/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class PredicateFor {

	@NotNull
	@Contract(pure = true)
	public static <T> Predicate<T> alwaysTrue() {
		return x -> true;
	}

	@NotNull
	@Contract(pure = true)
	public static <T> Predicate<T> alwaysFalse() {
		return x -> false;
	}

	@NotNull
	@Contract(pure = true)
	public static Predicate<Integer> isPalindromeInteger() {
		return x -> x.toString()
			.equals(
				new StringBuilder(x.toString())
					.reverse()
					.toString()
			);
	}
		
	@NotNull
	@Contract(pure = true)
	public static Predicate<Long> isMultipleOf(List<Long> values) {
		return isMultipleOf(values, alwaysFalse());
	}
	
	@NotNull
	@Contract(pure = true)
	public static Predicate<Long> isMultipleOf(List<Long> values, Predicate<Long> stoppingCondition) {
		return t -> {
			for (Long a: values) {
				if (stoppingCondition.test(a)) {
					return false;
				}
				if (t % a == 0) return true;
			}
			return false;
		};
	}
}