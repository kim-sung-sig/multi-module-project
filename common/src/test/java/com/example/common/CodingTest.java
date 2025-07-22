package com.example.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Solution {
	public int[] solution(String[] operations) {
		var map = new TreeMap<Integer, Integer>();
		for (var op : operations) {
			var arr = op.split(" ");
			var o = arr[0];
			var num = Integer.parseInt(arr[1]);

			if (o.equals("I")) map.merge(num, 1, Integer::sum);
			else if (!map.isEmpty()) {
				var key = num < 0 ? map.firstKey() : map.lastKey();
				map.computeIfPresent(key, (k, v) -> v > 1 ? v - 1 : null);
			}
		}

		return map.isEmpty() ? new int[]{0, 0} : new int[]{map.lastKey(), map.firstKey()};
	}
}

class CodingTest {

	@Test
	@DisplayName("Test")
	void test() {
		Solution solution = new Solution();

		String[] operations = {"I 16", "I -5643", "D -1", "D 1", "D 1", "I 123", "D -1"};
		int[] result = {0,0};
		int[] answer = solution.solution(operations);

		System.out.println("answer: " + Arrays.toString(answer));
		System.out.println("result:" + Arrays.toString(result));
		assertArrayEquals(result, answer);
	}
}
