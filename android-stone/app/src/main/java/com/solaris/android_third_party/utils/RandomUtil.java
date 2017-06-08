package com.solaris.android_third_party.utils;

import java.util.Random;

public class RandomUtil {
	static final char[] SEED = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~!@#$%^&*()-+<>"
			.toCharArray();
	static Random random = new Random();

	private static final char[] CHARS = new char[] { 'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd',
			'f', 'g', 'h', 'j', 'k', 'l', 'y', 'x', 'c', 'v', 'b', 'n', 'm', '\u00FC' };

	public static String generateRandomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(SEED[random.nextInt(SEED.length)]);
		return sb.toString();
	}

	/**
	 * Instantiates random long between given min and max
	 *
	 * @param min
	 *            the minimal value (including)
	 * @param max
	 *            the maximal value (excluding)
	 * @return random
	 */
	public static long randomBetween(long min, long max) {
		long rl = random.nextLong();
		if (rl >= min && rl < max) {
			return rl;
		} else {
			long between = Math.max(max - min, 1);
			return min + (Math.abs(rl % between));
		}
	}

	/**
	 * Instantiates random long between given min and max
	 *
	 * @param min
	 *            the minimal value
	 * @param max
	 *            the maximal value
	 * @return the random long created
	 */
	public static int randomBetween(int min, int max) {
		int ri = random.nextInt(Math.max(max - min, 1));
		return ri + min;
	}

	/**
	 * Instantiates random double given min and max
	 *
	 * @param min
	 *            the minimal value
	 * @param max
	 *            the maximal value
	 * @return the random double created
	 */
	public static double randomBetween(double min, double max) {
		double randomDouble = Math.random();
		double r = ((randomDouble) * (max - min));
		return r + min;
	}

	/**
	 * Instantiates random char
	 * 
	 * @return one random char
	 */
	public static char randomChar() {
		int positiveLong = randomBetween(0, CHARS.length);
		return CHARS[positiveLong];
	}

	/**
	 * Method initRandomLong.
	 *
	 * @return long
	 */
	public static long nextLong() {
		return random.nextLong();
	}

	public static boolean nextBoolean() {
		return random.nextBoolean();
	}

}
