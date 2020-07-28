package com.xxty.demoffmpeg.controller;

/**
 * @Author: llun
 * @DateTime: 2020/7/28 12:13
 * @Description: TODO
 */
public class Result {
    public static Result fail(String str) {
        System.out.println(str);
        return new Result();
    }

    public static Result suc(String success) {
        System.out.println(success);
        return new Result();
    }
}
