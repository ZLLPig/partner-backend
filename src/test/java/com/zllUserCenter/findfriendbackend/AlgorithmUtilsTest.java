package com.zllUserCenter.findfriendbackend;

import org.junit.jupiter.api.Test;
import utils.AlgorithmUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlgorithmUtilsTest {

    @Test
    void test(){
        String str1 = "zll是美女";
        String str2 = "zll不是美女";
        String str3 = "zll是不是美女";
        int score1 = AlgorithmUtils.minDistance(str1, str2);
        int score2 = AlgorithmUtils.minDistance(str1, str3);
        System.out.println(score1);
        System.out.println(score2);
    }

    @Test
    void test2(){
        List<String> tagList1 = Arrays.asList("java","大一","女");
        List<String> tagList2 = Arrays.asList("c++","大一","男");
        List<String> tagList3 = Arrays.asList("c++","大二","女");
        int score1 = AlgorithmUtils.minDistance(tagList1, tagList2);
        int score2 = AlgorithmUtils.minDistance(tagList1, tagList3);
        System.out.println(score1);
        System.out.println(score2);
    }

}
