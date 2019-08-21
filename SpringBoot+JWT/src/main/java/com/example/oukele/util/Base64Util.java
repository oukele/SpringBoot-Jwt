package com.example.oukele.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Util {

    private static final String charset = "utf-8";

    /**
     * 解密
     * @param data
     * @return
     */
    public static String decode(String data) {
        try {
            if (null == data) {
                return null;
            }
            return new String(Base64.decodeBase64(data.getBytes(charset)), charset);
        } catch (UnsupportedEncodingException e) {
            String format = String.format("字符串：%s，解密异常", data);
            System.out.println( format + " <-----> " + e.getMessage() );
        }

        return null;
    }

    /**
     * 加密
     * @param data
     * @return
     */
    public static String encode(String data) {
        try {
            if (null == data) {
                return null;
            }
            return new String(Base64.encodeBase64(data.getBytes(charset)), charset);
        } catch (UnsupportedEncodingException e) {
            String format = String.format("字符串：%s，解密异常", data);
            System.out.println( format + " <-----> " + e.getMessage() );
        }

        return null;
    }


}
