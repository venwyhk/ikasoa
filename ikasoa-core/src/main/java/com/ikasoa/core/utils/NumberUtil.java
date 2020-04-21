package com.ikasoa.core.utils;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import lombok.experimental.UtilityClass;

/**
 * Number工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.3
 */
@UtilityClass
public class NumberUtil {

	private final static ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

	private final static int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

	public static int getRandomInt(int min, int max) {
		return RANDOM.nextInt(max) % (max - min + 1) + min;
	}

	public static long getRandomLong(long bound) {
		long b, l;
		do {
			b = (RANDOM.nextLong() << 1) >>> 1;
			l = b % bound;
		} while (b - l + (bound - 1) < 0L);
		return l;
	}

	public static float getRandomFloat(float bound, int scale) {
		return getRandomFloat(bound, scale, ROUNDING_MODE);
	}

	public static float getRandomFloat(float bound, int scale, int roundingMode) {
		return new BigDecimal(RANDOM.nextFloat() * bound).setScale(scale, roundingMode).floatValue();
	}

	public static double getRandomDouble(double bound, int scale) {
		return getRandomDouble(bound, scale, ROUNDING_MODE);
	}

	public static double getRandomDouble(double bound, int scale, int roundingMode) {
		return getDouble(RANDOM.nextDouble() * bound, scale, roundingMode);
	}

	public static double getDouble(double d, int scale) {
		return getDouble(d, scale, ROUNDING_MODE);
	}

	public static double getDouble(double d, int scale, int roundingMode) {
		return new BigDecimal(d).setScale(scale, roundingMode).doubleValue();
	}

}
