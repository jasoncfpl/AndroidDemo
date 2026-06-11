package com.example.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * @author li jia
 * @date 2026/4/9 18:54
 * @description:
 */
public class StringTest {

    /**
     * 批量字符串替换（内存友好，避免 OOM）
     * @param input 原始字符串
     * @param replaceMap 要替换的键值对：oldStr -> newStr
     * @return 替换完成后的字符串
     */
    public static String batchReplace(String input, Map<String, String> replaceMap) {
        if (input == null || input.isEmpty() || replaceMap == null || replaceMap.isEmpty()) {
            return input;
        }

        StringBuilder sb = new StringBuilder(input);

        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            String oldStr = entry.getKey();
            String newStr = entry.getValue();

            if (oldStr == null || oldStr.isEmpty() || newStr == null) {
                continue;
            }

            int start = 0;
            int oldLength = oldStr.length();

            while (start < sb.length()) {
                int index = sb.indexOf(oldStr, start);
                if (index == -1) {
                    break;
                }

                // 执行替换（原地修改，不创建新字符串）
                sb.replace(index, index + oldLength, newStr);

                // 移动指针，避免重复替换
                start = index + newStr.length();
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        String input = "hello world hello 400-8178-168 world hello world hello world hello world inke world hello world hello world hello world hello world 映客 world hello 小映 hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hellohttp://img.ikstatic.cn/MTU4OTg4OTU2NzUzNCM0NjcjanBn.jpg world hello world";

        String MJ_DEFAULT_HEAD = "https://img.ikstatic.cn/MTY5NzQ0ODg1NDMyMSM5NDIjcG5n.png";
        HashMap<String, String> replaceMap = new HashMap<>();
        replaceMap.put("映客", "咪鸭");
        replaceMap.put("映币", "蜜豆");
        replaceMap.put("小映", "");
        replaceMap.put("400-8178-168", "400-9601-220");
        replaceMap.put("http://img.ikstatic.cn/MTU4OTg4OTU2NzUzNCM0NjcjanBn.jpg", MJ_DEFAULT_HEAD);





        String output = batchReplace(input, replaceMap);
        System.out.println(output);

    }
}
