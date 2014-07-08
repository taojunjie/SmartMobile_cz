package com.android.smartmobile.cz.util;


import java.math.BigDecimal;
import java.util.StringTokenizer;

public class StringUtil {

	/**
	 * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
	 */
	private StringUtil() {
	}

	/**
	 * 此方法将给出的字符串source使用delim划分为单词数组。
	 * 
	 * @param source
	 *            需要进行划分的原字符串
	 * @param delim
	 *            单词的分隔字符串
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组，
	 *         如果delim为null则使用逗号作为分隔字符串。
	 * @since 0.1
	 */
	public static String[] split(String source, String delim) {
		String[] wordLists;
		if (source == null) {
			wordLists = new String[1];
			wordLists[0] = source;
			return wordLists;
		}
		if (delim == null) {
			delim = ",";
		}
		StringTokenizer st = new StringTokenizer(source, delim);
		int total = st.countTokens();
		wordLists = new String[total];
		for (int i = 0; i < total; i++) {
			wordLists[i] = st.nextToken();
		}
		return wordLists;
	}

	/**
	 * 此方法将给出的字符串source使用delim划分为单词数组。
	 * 
	 * @param source
	 *            需要进行划分的原字符串
	 * @param delim
	 *            单词的分隔字符
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
	 * @since 0.2
	 */
	public static String[] split(String source, char delim) {
		return split(source, String.valueOf(delim));
	}

	/**
	 * 此方法将给出的字符串source使用逗号划分为单词数组。
	 * 
	 * @param source
	 *            需要进行划分的原字符串
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
	 * @since 0.1
	 */
	public static String[] split(String source) {
		return split(source, ",");
	}

	/**
	 * 此方法将 String 转化为 double
	 * 
	 * @param source
	 * @return
	 */
	public static double String2Double(String source) {
		BigDecimal bigDecimal = new BigDecimal(source);
		return bigDecimal.doubleValue();
	}

	/**
	 * 此方法将 String 转化为 int
	 * 
	 * @param source
	 * @return
	 */
	public static int String2Int(String source) {
		if (!StringUtil.isEmpty(source)) {
			return Integer.parseInt(source);
		} else {
			return 0;
		}
	}

	/**
	 * 此方法将 String数组 转化为 double数组
	 * 
	 * @param source
	 * @return
	 */
	public static double[] Strings2Doubles(String[] source) {

		double[] d = new double[source.length];
		for (int i = 0; i < source.length; i++) {
			d[i] = String2Double(source[i]);
		}
		return d;
	}

	/**
	 * 此方法将 String 转化为 int
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isEmpty(String source) {
		if (source != null && !source.trim().equalsIgnoreCase("")) {
			return false;
		} else {
			return true;
		}

	}
}
