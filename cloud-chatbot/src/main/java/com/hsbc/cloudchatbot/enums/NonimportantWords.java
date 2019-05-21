package com.hsbc.cloudchatbot.enums;

/**
 * Created by nicole on 2019/5/21.
 */
public enum NonimportantWords {
    IS("是"), WHAT("什么"), HOW("如何"), DE("的");

    private String word;

    // 构造方法
    private NonimportantWords(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }


}
