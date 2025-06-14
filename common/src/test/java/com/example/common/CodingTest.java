package com.example.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Solution {
    public int solution(int number, int limit, int power) {
        int answer = 0;

        // 에라토스테네스 체
        int[] divisorCount = new int[number + 1];
        for (int i = 1; i <= number; i++) {
            for (int j = i; j <= number; j += i) {
                divisorCount[j]++;
            }
        }

        for (int i = 1; i <= number; i++) {
            int numberOfDivisors = divisorCount[i];
            answer += (numberOfDivisors > limit)
                    ? power
                    : numberOfDivisors;
        }

        return answer;
    }
}

class CodingTest {

    @Test
    @DisplayName("Test")
    void test() {
        Solution solution = new Solution();

        int number = 5, limit = 3, power = 2, result = 10;
        int answer = solution.solution(number, limit, power);

        System.out.println("answer: " + answer);
        System.out.println("result:" + result);
        Assertions.assertEquals(result, answer);
    }
}
