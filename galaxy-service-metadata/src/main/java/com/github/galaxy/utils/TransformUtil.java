package com.github.galaxy.utils;


import com.alibaba.fastjson.JSONObject;
import com.github.galaxy.config.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 编码功能
 */
@Slf4j
public class TransformUtil {

	private static String[] hexDigits = new String[]
			{ "0", "1", "2", "3", "4", "5", "6", "7", "8",
			  "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 获取字符串的唯一标识符
	 * @param target
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String target) throws NoSuchAlgorithmException {
		if(null==target){
			log.warn("Null String has no md5 string.");
			return null;
		}
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] digest = md5.digest(target.getBytes());
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			buffer.append(byteToHexString(digest[i]));
		}
		return buffer.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n / 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 将键值对转化为json字符串
	 * @param info
	 * @return
	 */
	public static String toJsonString(Map info){
		if(info.size()> Configuration.DEFAULT_MAX_JSON_ENTRIES){
			log.warn("Can not transform big map into json due to the limit.");
			return null;
		}
		JSONObject jsonObject = new JSONObject(info);
		return jsonObject.toJSONString();
	}

	public static Map jsonToMap(String json){
		return JSONObject.parseObject(json,Map.class);
	}
}
