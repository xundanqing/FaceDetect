package com.example.facedetectver4.util;


public class Configutil {

    private   String    Appid;
    private   String    Devid;
    private   static    Configutil  configutil;

    public static Configutil  getintance(){
        if(configutil == null){
            configutil  =  new Configutil();
        }
        return  configutil;
    }

    public   String  getAppid(){
        return  "FSrV9ycuwAFtvwCjRSYG8sGJtPoGWsNzEgjYWoacWbzF";
    }

    public   String  getDevid(){
        return  "53YwdZGu8VoxuRL988eHmx8YemAqxJUb8ELueCn3ZXwN";
    }
}
