package com.zkdesign.zookeeper;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class Test {


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


        unitTOEngDic.put(0, "ty");
        unitTOEngDic.put(1, "Hundred");
        unitTOEngDic.put(2, "Thousand");
        unitTOEngDic.put(3, "Million");
        unitTOEngDic.put(4, "Billion");
    }


    public static String convert(Integer number) {
        String[] numberStr = String.valueOf(number).split("");

//        System.out.println("numberStr:" + numberStr);
//        for (String s : numberStr) {
//            System.out.println(s);
//        }

        List<String> result = new ArrayList<>();
        for (int u = numberStr.length - 1, j = 1; u >= 0; u--, j++) {
//            result.add(0, numToEngDic.get(Integer.valueOf(numberStr[u])));

            System.out.println("u:" + j);

            if (j == 1) {
                result.add(0, numToEngDic.get(Integer.valueOf(numberStr[u])));
            } else if (j == 2) {
                result.add(0, numToEngDic.get(Integer.valueOf(numberStr[u])) + "ty");
                result.add(1, "十位");
            } else {
                Integer uu = Integer.valueOf(j / 3);
                if (unitTOEngDic.containsKey(Integer.valueOf(uu))) {
                    result.add(0, unitTOEngDic.get(Integer.valueOf(uu)));
                } else {
                    result.add(0, "unknown");
                }
                result.add(0, numToEngDic.get(Integer.valueOf(numberStr[u])));
            }
//            if (uu > 0) {
//
//            }
        }


        return String.join(" ", result);
    }


    public static String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        StringBuilder b = new StringBuilder();
        String str = String.valueOf(num);
        if (str.length() > 9) {
            b.append(toEnglish(str.substring(0, str.length() - 9))).append("Billion ");
            str = str.substring(str.length() - 9);
        }
        if (str.length() > 6) {
            String s = toEnglish(str.substring(0, str.length() - 6));
            if (!s.trim().equals("")) {
                b.append(s).append("Million ");
            }
            str = str.substring(str.length() - 6);
        }
        if (str.length() > 3) {
            String s = toEnglish(str.substring(0, str.length() - 3));
            if (!s.trim().equals("")) {
                b.append(s).append("Thousand ");
            }
            str = str.substring(str.length() - 3);
        }
        b.append(toEnglish(str));
        String res = b.toString().trim();
        while (res.contains("  ")) {
            res = res.replace("  ", " ");
        }
        return res;
    }


    private static String toEnglish(String str) {
        StringBuilder b = new StringBuilder();
        if (str.length() > 2) {
            char c = str.charAt(str.length() - 3);
            if (c != '0') {
                b.append(getNum(c)).append(" Hundred ");
            }
        }
        boolean flag = true;
        if (str.length() > 1) {
            char c = str.charAt(str.length() - 2);
            if (c != '0') {
                if (c == '1') {
                    flag = false;
                    b.append(getNumteen(str.charAt(str.length() - 1))).append(" ");
                } else {
                    b.append(getNumty(c)).append(" ");
                }
            }
        }
        if (str.length() > 0 && flag) {
            char c = str.charAt(str.length() - 1);
            b.append(getNum(c)).append(" ");
        }
        return b.toString();
    }

    private static String getNum(char c) {
        switch (c) {
            case '0':
                return "";
            case '1':
                return "One";
            case '2':
                return "Two";
            case '3':
                return "Three";
            case '4':
                return "Four";
            case '5':
                return "Five";
            case '6':
                return "Six";
            case '7':
                return "Seven";
            case '8':
                return "Eight";
            case '9':
                return "Nine";
            default:
                return "";
        }
    }

    private static String getNumty(char c) {
        switch (c) {
            case '2':
                return "Twenty";
            case '3':
                return "Thirty";
            case '4':
                return "Forty";
            case '5':
                return "Fifty";
            case '6':
                return "Sixty";
            case '7':
                return "Seventy";
            case '8':
                return "Eighty";
            case '9':
                return "Ninety";
            default:
                return "";
        }
    }

    private static String getNumteen(char c) {
        switch (c) {
            case '0':
                return "Ten";
            case '1':
                return "Eleven";
            case '2':
                return "Twelve";
            case '3':
                return "Thirteen";
            case '4':
                return "Fourteen";
            case '5':
                return "Fifteen";
            case '6':
                return "Sixteen";
            case '7':
                return "Seventeen";
            case '8':
                return "Eighteen";
            case '9':
                return "Nineteen";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        String test1 = convert(123);
        System.out.println("test1:" + test1);
        String test2 = convert(12345);
        System.out.println("test2:" + test2);

        String test3 = numberToWords(123);

        System.out.println(test3);
    }
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