package com.example.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class Solution {
	public int solution(int n, int w, int num) {
		int floor = (num - 1) / w + 1; // 물건의 층수
		int totalFloors = (n - 1) / w + 1; // 최고층수

		// 최고층인 경우
		if (floor == totalFloors) return 1;

		int tempIndex = (num - 1) % w;
		int index = floor % 2 == 0 ? (w - 1) - tempIndex : tempIndex;

		int lastTempIndex = (n - 1) % w;
		int lastIndex = totalFloors % 2 == 0 ? (w - 1) - lastTempIndex : lastTempIndex;

		int k = (totalFloors % 2 == 0 && index < lastIndex) || (totalFloors % 2 == 1 && lastIndex < index) ? -1 : 0;
		return totalFloors - floor + 1 + k;
	}
}

class CodingTest {

	@Test
	@DisplayName("Test")
	void test() {
		int n = 22, w = 6, num = 8, result = 3;
		Solution solution = new Solution();

		int answer = solution.solution(n, w, num);

		System.out.println("answer: " + answer);
		System.out.println("result:" + result);
		assertEquals(result, answer);
	}
}