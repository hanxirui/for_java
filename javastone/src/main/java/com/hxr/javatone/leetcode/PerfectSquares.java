package com.hxr.javatone.leetcode;

/**
 * Given a positive integer n, find the least number of perfect square numbers
 * (for example, 1, 4, 9, 16, ...) which sum to n.
 * 
 * For example, given n = 12, return 3 because 12 = 4 + 4 + 4; given n = 13,
 * return 2 because 13 = 4 + 9. 279. Perfect Squares
 * 
 * @author hanxirui
 *
 */
public class PerfectSquares {
	static boolean is_square(int n) {
		int temp = (int) Math.sqrt(n);
		return temp * temp == n;
	}

	static int numSquares(int n) {
		while ((n & 3) == 0) {
			n >>= 2;
		}
		if ((n & 7) == 7) {
			return 4; // n % 8 == 7
		}
		if (is_square(n)) {
			return 1;
		}
		int sqrt_n = (int) Math.sqrt(n);
		for (int i = 1; i <= sqrt_n; i++) {
			if (is_square(n - i * i)) {
				return 2;
			}
		}
		return 3;
	}

	static int numSquares2(int n) {
		int[] dp = new int[n + 1];
		dp[1] = 1;
		for (int i = 2; i <= n; i++) {
			int min = Integer.MAX_VALUE;
			int j = 1;
			while (j * j <= i) {
				if (j * j == i) {
					min = 1;
					break;
				}
				min = Math.min(min, dp[i - j * j] + 1);
				++j;
			}
			dp[i] = min;
		}
		return dp[n];
	}

	public static void main(String[] args) {
		System.out.println(numSquares(81));
		System.out.println(numSquares(82));
		System.out.println(numSquares2(83));
		System.out.println(numSquares2(84));
		System.out.println(numSquares2(85));
	}
}
