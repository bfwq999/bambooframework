package org.bambooframework.common;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音工具类
 * @author lei
 * @date 2015年8月18日
 * @Description:
 */
public class PinyinUtil {
	
	static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	static {
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}
	
	/**
	 * 汉字转换为汉语拼音首字母，英文字符不变
	 * @author lei
	 * @param chines 汉字
	 * @return 拼音(小写)，每个汉字的首字母
	 * @date 2015年8月18日
	 * @Description:
	 */
	public static String getHalfPinyin(String chines) {
		if(chines==null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String tempPinyin = null;
		for (int i = 0; i < chines.length(); ++i) {
			tempPinyin = getCharacterPinYin(chines.charAt(i));
			if (tempPinyin == null)
				sb.append(Character.toLowerCase(chines.charAt(i)));
			else
				sb.append(tempPinyin.charAt(0));
		}
		return sb.toString();
	}

	/**
	 * 汉字转换为汉语拼音，英文字符不变
	 * @author lei
	 * @param chines 汉字
	 * @return 拼音（小写）
	 * @date 2015年8月18日
	 * @Description:
	 */
	public static String getFullPinyin(String chines) {
		if(chines==null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String tempPinyin = null;
		for (int i = 0; i < chines.length(); ++i) {
			tempPinyin = getCharacterPinYin(chines.charAt(i));
			if (tempPinyin == null)
				sb.append(Character.toLowerCase(chines.charAt(i)));
			else
				sb.append(tempPinyin);
		}
		return sb.toString();
	}
	/**
	 * 单个汉字转换
	 * @author lei
	 * @param c
	 * @return
	 * @date 2015年8月18日
	 * @Description:
	 */
	public static String getCharacterPinYin(char c) {
		try {
			String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
			if(pinyin!=null)
				return pinyin[0];
			else
				return null;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			return null;
		}
	}
}
