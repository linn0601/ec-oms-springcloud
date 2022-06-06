package org.linn.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 关于谓词的测试
 */
@SpringBootTest
class PredicateTest {

	public static List<String> MICRO_SERVICE = Arrays.asList("nacos", "authority", "gate", "ribbon", "feign",
		"hystrix", "stream");

	@Test
	void predicateTest() {
		Predicate<String> letterLengthLimit = s -> s.length() > 5;

		MICRO_SERVICE.stream().filter(letterLengthLimit).forEach(System.out::println);

		Assertions.assertTrue(true);
	}

	/**
	 * &&
	 */
	@Test
	void predicateAndTest() {
		Predicate<String> letterLengthLimit = s -> s.length() > 5;
		Predicate<String> letterStartWith = s -> s.startsWith("gate");

		MICRO_SERVICE.stream().filter(
			letterLengthLimit.and(letterStartWith)
		).forEach(System.out::println);

		Assertions.assertTrue(true);

	}

	/**
	 * ||
	 */
	@Test
	void predicateOrTest() {

		Predicate<String> letterLengthLimit = s -> s.length() > 5;
		Predicate<String> letterStartWith = s -> s.startsWith("gate");

		List<String> result = MICRO_SERVICE.stream().filter(
			letterLengthLimit.or(letterStartWith)
		).collect(Collectors.toList());

		Assertions.assertTrue(result.isEmpty());
	}

	/**
	 * !
	 */
	@Test
	void predicateNegateTest() {

		Predicate<String> letterLengthLimit = s -> s.length() > 5;
		Predicate<String> letterStartWith = s -> s.startsWith("gate");

		MICRO_SERVICE.stream().filter(
			letterLengthLimit.negate()
				.and(letterStartWith)
		).forEach(System.out::println);

		Assertions.assertTrue(true);
	}

	/**
	 * isEqual() 先会判断对象是否为NULL，在使用equals比较
	 */
	@Test
	void predicateIsEqualTest() {

		Predicate<String> isEqual = s -> Predicate.isEqual("gate").test(s);

		MICRO_SERVICE.stream().filter(
			isEqual
		).forEach(System.out::println);

		Assertions.assertTrue(true);
	}
}
