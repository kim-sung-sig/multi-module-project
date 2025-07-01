package com.example.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Solution {
    public int[] solution(String s) {
        s = s.substring(2, s.length() - 2);
        String[] sets = s.split("},\\{");

        Arrays.sort(sets, Comparator.comparingInt(String::length));

        System.out.println(Arrays.toString(sets));

        List<String> result = new ArrayList<>();
        Set<String> mem = new HashSet<>();

        for (String set : sets) {
            String[] nums = set.split(",");
            for (String num : nums) if (mem.add(num)) result.add(num);
        }

        return result.stream().mapToInt(Integer::valueOf).toArray();
    }
}

class CodingTest {

    @Test
    @DisplayName("Test")
    void test() {
        Solution solution = new Solution();

        String s = "{{2},{2,1},{2,1,3},{2,1,3,4}}";
        int[] result = {2, 1, 3, 4};
        int[] answer = solution.solution(s);

        System.out.println("answer: " + Arrays.toString(answer));
        System.out.println("result:" + Arrays.toString(result));
        Assertions.assertEquals(result, answer);
    }
}
