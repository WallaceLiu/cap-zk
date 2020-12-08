package com.zkdesign.zookeeper;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class example01 {

//    public static void main(String[] args) {
//        System.out.println("x");
//    }


    public static Map<Integer, String> numToEngDic = new HashMap<>();
    public static Map<Integer, String> unitTOEngDic = new HashMap<>();

    static {
        numToEngDic.put(1, "one");
        numToEngDic.put(2, "two");
        numToEngDic.put(3, "three");
        numToEngDic.put(4, "four");
        numToEngDic.put(5, "five");
        numToEngDic.put(6, "six");
        numToEngDic.put(7, "seven");
        numToEngDic.put(8, "eight");
        numToEngDic.put(9, "nive");
        numToEngDic.put(0, "zeor");


        unitTOEngDic.put(0, "Hundred");
        unitTOEngDic.put(1, "Thousand");
        unitTOEngDic.put(2, "Million");
        unitTOEngDic.put(3, "Billion");
    }


    public String convert(Integer number) {
        String[] numberStr = String.valueOf(number).split("");

        List<String> result = new ArrayList<>();
        for (int u = numberStr.length; u >= 0; u++) {
            result.add(0, numToEngDic.get(numberStr[u]));
            if (unitTOEngDic.containsKey(Integer.valueOf(u / 3))) {
                result.add(0, unitTOEngDic.get(Integer.valueOf(u / 3)));
            } else {
                result.add(0, "unknown");
            }
        }


        return String.join(" ", result);
    }
//    Map<Integer,String> dic=new HashMap<>();
}


//Example 1:
//        Input: 123
//        Output: "One Hundred Twenty Three"
//
//        Example 2:
//        Input: 12345
//        Output: "Twelve Thousand Three Hundred Forty Five"
//        Example 3:
//        Input: 1234567
//        Output: "One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"
//
//        Example 4:
//        Input: 1234567891
//        Output: "One Billion Two Hundred Thirty Four Million Five Hundred Sixty Seven Thousand Eight Hundred Ninety One"