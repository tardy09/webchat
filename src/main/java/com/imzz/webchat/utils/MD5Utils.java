package com.imzz.webchat.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.security.MessageDigest;

public class MD5Utils {

	/**
	 * @Description: 对字符串进行md5加密 
	 */
	public static String getMD5Str(String strValue) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
		return newstr;
	}

	/**
	 * 利用SHIRO 进行MD5加密
	 * @param password  用户的明文密码
	 * @param salt  用户密码加密所需的盐
	 * @return
	 */
	public static String getPwd(String password,String salt)
	{
		String newMd5Password = new SimpleHash("MD5", password, salt,  1024).toHex();
		return newMd5Password;
	}

    public static void main(String[] args) {
        System.out.println(getPwd("123456","567"));
    }
}
