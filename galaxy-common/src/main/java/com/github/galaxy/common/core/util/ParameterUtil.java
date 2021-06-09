package com.github.galaxy.common.core.util;

import com.github.galaxy.common.core.config.ConfigureReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.zip.CheckedInputStream;

public class ParameterUtil {

	private static ConfigureReader reader=ConfigureReader.getReader();

	private static Logger logger= LoggerFactory.getLogger(ParameterUtil.class);

	// 辅助反序列化
	private static CheckedInputStream inputStream;

	public static String toString(List list){
		StringBuffer sb=new StringBuffer();
		list.forEach( x ->sb.append(x.toString()));
		return sb.toString();
	}

	// TODO 实现CRC32校验
	public static long getCheckSum(Serializable context){
		return 0;
	}

	public static String toCamelCase(String str){
		char[] chars = str.toLowerCase().toCharArray();
		if(!(chars[0]<='z' && chars[0]>='a'))
			return null;
		chars[0]= (char) (chars[0]-'a'+'A');
		return new String(chars);
	}

}
